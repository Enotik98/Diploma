package com.example.bookstore_app.service;

import com.example.bookstore_app.entity.Book;
import com.example.bookstore_app.entity.OrderStatus;
import com.example.bookstore_app.entity.Sale;
import com.example.bookstore_app.entity.SaleBook;
import com.example.bookstore_app.repository.SaleRepository;
import com.example.bookstore_app.utils.CustomException;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AnalitycsService {
    @Autowired
    private SaleRepository saleRepository;
    private static final double SMOOTHING_FACTOR = 0.2;

    public Map<Book, Integer> forecastDemandTrends() {
        List<Sale> salesList = saleRepository.findSalesByStatus(OrderStatus.COMPLETED);

        Map<Book, Integer> demandTrends = analyzeDemand(salesList);

        Map<Book, Integer> demandForecast = forecastFutureDemand(demandTrends);

        return demandForecast;
    }
    private Map<Book, Integer> analyzeDemand(List<Sale> salesList) {
        Map<Book, Integer> demandMap = new HashMap<>();
        for(Sale sale: salesList) {
            List<SaleBook> saleBooks = sale.getSaleBooks();
            for (SaleBook saleBook: saleBooks) {
                Book book = saleBook.getBook();
                int quantitySold = saleBook.getQuantity();
                demandMap.put(book, demandMap.getOrDefault(book, 0) + quantitySold);
            }
        }
        return demandMap;
    }
    private Map<Book, Integer> forecastFutureDemand(Map<Book, Integer> demandTrends) {
        Map<Book, Integer> forecast = new HashMap<>();

        for (Map.Entry<Book, Integer> entry : demandTrends.entrySet()) {
            Book book = entry.getKey();
            int currentDemand = entry.getValue();

            // Використання експоненційного згладжування для прогнозування
            int forecastedDemand = (int) (currentDemand * (1 - SMOOTHING_FACTOR) + (forecast.getOrDefault(book, 0) * SMOOTHING_FACTOR));

            forecast.put(book, forecastedDemand);
        }
        return forecast;
    }

    public Map<Book, Double> forecastDemandTrendsARIMA() {
        List<Sale> salesList = saleRepository.findSalesByStatus(OrderStatus.COMPLETED);

        Map<Book, List<Integer>> demandTrends = analyzeDemandARIMA(salesList);
        System.out.println(demandTrends);

        Map<Book, Double> demandForecast = forecastFutureDemandARIMA(demandTrends);

        return demandForecast;
    }
    private Map<Book, List<Integer>> analyzeDemandARIMA(List<Sale> salesList) {
        Map<Book, List<Integer>> demandMap = new HashMap<>();

        // Групуємо продажі по місяцях для кожної книги
        Map<Book, Map<Integer, Integer>> monthlyDemandMap = new HashMap<>();

        for (Sale sale : salesList) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(sale.getOrderDate());
            int month = calendar.get(Calendar.MONTH) + 1;
            int year = calendar.get(Calendar.YEAR);

            for (SaleBook saleBook : sale.getSaleBooks()) {
                Book book = saleBook.getBook();
                int quantitySold = saleBook.getQuantity();

                monthlyDemandMap.putIfAbsent(book, new HashMap<>());
                int previousQuantity = monthlyDemandMap.get(book).getOrDefault(year * 100 + month, 0);
                monthlyDemandMap.get(book).put(year * 100 + month, previousQuantity + quantitySold);
            }
        }

        // Перетворюємо на Map<Book, List<Integer>>
        for (Map.Entry<Book, Map<Integer, Integer>> entry : monthlyDemandMap.entrySet()) {
            Book book = entry.getKey();
            Map<Integer, Integer> monthlyDemand = entry.getValue();

            // Сортуємо за ключем (рік і місяць) і додаємо в список
            List<Integer> sortedDemand = monthlyDemand.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .map(Map.Entry::getValue)
                    .collect(Collectors.toList());

            demandMap.put(book, sortedDemand);
        }

        return demandMap;
    }

    private Map<Book, Double> forecastFutureDemandARIMA(Map<Book, List<Integer>> demandHistory) {
        Map<Book, Double> forecast = new HashMap<>();

        for (Map.Entry<Book, List<Integer>> entry : demandHistory.entrySet()) {
            Book book = entry.getKey();
            List<Integer> demandData = entry.getValue();

            double[] demandArray = demandData.stream().mapToDouble(i -> i).toArray();
            double forecastedDemand = forecastARIMA(demandArray);

            forecast.put(book, forecastedDemand);
        }

        return forecast;
    }
    private double forecastARIMA(double[] demandArray) {
        // Якщо ряд має недостатньо даних для прогнозування, просто повертаємо середнє значення
        if (demandArray.length < 2) {
            return Arrays.stream(demandArray).average().orElse(0.0);
        }

        // Створюємо об'єкт моделі SimpleRegression
        SimpleRegression regression = new SimpleRegression();

        // Додаємо дані про попит у модель
        for (int i = 0; i < demandArray.length; i++) {
            regression.addData(i + 1, demandArray[i]);
        }

        // Прогнозуємо майбутнє значення
        double nextDemand = regression.predict(demandArray.length + 1);

        return nextDemand;
    }
    public ByteArrayOutputStream generateForecastDemandExcel(Map<Book, Double> forecastDemandARIMA, Map<Book, Integer> demandForecast) {
        try(XSSFWorkbook workbook = new XSSFWorkbook(); ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            XSSFSheet sheet = workbook.createSheet("ARIMA");
            XSSFRow headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Book");
            headerRow.createCell(1).setCellValue("Forecast Demand");
            int rowNum = 1;
            for (Map.Entry<Book, Double> entry: forecastDemandARIMA.entrySet()) {
                XSSFRow row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(entry.getKey().getTitle());
                row.createCell(1).setCellValue(entry.getValue());
            }

            sheet = workbook.createSheet("exponential smoothing");
            headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Book");
            headerRow.createCell(1).setCellValue("Forecast Demand");
            rowNum = 1;
            for (Map.Entry<Book, Integer> entry: demandForecast.entrySet()) {
                XSSFRow row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(entry.getKey().getTitle());
                row.createCell(1).setCellValue(entry.getValue());
            }

            List<Sale> sales = saleRepository.findSalesByStatus(OrderStatus.COMPLETED);

            sheet = workbook.createSheet("Sale statistics");
            XSSFRow row = sheet.createRow(0);
            row.createCell(0).setCellValue("VarianceFromSales");
            row.createCell(1).setCellValue(calculateVarianceFromSales(sales));

            row = sheet.createRow(1);
            row.createCell(0).setCellValue("StandardDeviation");
            row.createCell(1).setCellValue(calculateStandardDeviation(sales));

            row = sheet.createRow(2);
            row.createCell(0).setCellValue("Mode");
            row.createCell(1).setCellValue(calculateModeFromSales(sales));

            row = sheet.createRow(3);
            row.createCell(0).setCellValue("Skewness");
            row.createCell(1).setCellValue(calculateSkewnessFromSales(sales));

            row = sheet.createRow(4);
            row.createCell(0).setCellValue("Quantile (0.25)");
            row.createCell(1).setCellValue(calculateQuantileFromSales(sales, 0.25));

            row = sheet.createRow(5);
            row.createCell(0).setCellValue("Quantile (0.5)");
            row.createCell(1).setCellValue(calculateQuantileFromSales(sales, 0.5));

            row = sheet.createRow(6);
            row.createCell(0).setCellValue("Quantile (0.75)");
            row.createCell(1).setCellValue(calculateQuantileFromSales(sales, 0.75));

            row = sheet.createRow(7);
            row.createCell(0).setCellValue("Correlation");
            row.createCell(1).setCellValue(calculateCorrelationFromSales(sales));


            workbook.write(outputStream);
            return outputStream;
        }catch (Exception e){
            throw new CustomException(HttpStatus.BAD_REQUEST, e.getMessage());
        }

    }

    private double calculateVarianceFromSales(List<Sale> sales) {
        List<Double> amounts = sales.stream()
                .map(sale -> (double) sale.getAmount() / 100) // Використовуємо лямбда-вираз
                .collect(Collectors.toList());
        return calculateVariance(amounts);
    }
    private double calculateVariance(List<Double> data) {
        double mean = calculateMean(data);
        double sumSquaredDiff = data.stream().mapToDouble(x -> Math.pow(x - mean, 2)).sum();
        return sumSquaredDiff / (data.size() - 1); // Виправлення з data.size() - 1
    }

    private double calculateMean(List<Double> data) {
        return data.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
    }
    private double calculateStandardDeviation(List<Sale> sales) {
        List<Double> amounts = sales.stream()
                .map(sale -> (double) sale.getAmount() / 100) // Використовуємо лямбда-вираз
                .collect(Collectors.toList());
        double variance = calculateVariance(amounts); // Обчислюємо дисперсію
        return Math.sqrt(variance); // Повертаємо квадратний корінь з дисперсії
    }

    private double calculateModeFromSales(List<Sale> sales) {
        List<Double> amounts = sales.stream()
                .map(sale -> (double) sale.getAmount() / 100)
                .collect(Collectors.toList());

        return calculateMode(amounts);
    }
    private double calculateMode(List<Double> data) {
        Map<Double, Integer> frequencyMap = new HashMap<>();

        // Підрахунок частоти кожного значення
        for (Double value : data) {
            frequencyMap.put(value, frequencyMap.getOrDefault(value, 0) + 1);
        }

        // Знаходження максимальної частоти
        int maxFrequency = Collections.max(frequencyMap.values());

        // Додавання значень з максимальною частотою до списку мод
        List<Double> modeValues = new ArrayList<>();
        for (Map.Entry<Double, Integer> entry : frequencyMap.entrySet()) {
            if (entry.getValue() == maxFrequency) {
                modeValues.add(entry.getKey());
            }
        }

        // Якщо є декілька мод, вибираємо перше знайдене значення моди
        return modeValues.isEmpty() ? 0.0 : modeValues.get(0);
    }
    private double calculateSkewnessFromSales(List<Sale> sales) {
        List<Double> amounts = sales.stream()
                .map(sale -> (double) sale.getAmount() / 100)
                .collect(Collectors.toList());
        return calculateSkewness(amounts);
    }

    private double calculateSkewness(List<Double> data) {
        double mean = calculateMean(data);
        double stdDev = calculateStandardDeviationFromData(data);
        int n = data.size();

        double skewness = data.stream()
                .mapToDouble(x -> Math.pow((x - mean) / stdDev, 3))
                .sum();

        return (n * skewness) / ((n - 1) * (n - 2));
    }

    private double calculateStandardDeviationFromData(List<Double> data) {
        double variance = calculateVariance(data);
        return Math.sqrt(variance);
    }

    private double calculateQuantileFromSales(List<Sale> sales, double quantile) {
        List<Double> amounts = sales.stream()
                .map(sale -> (double) sale.getAmount() / 100)
                .collect(Collectors.toList());
        return calculateQuantile(amounts, quantile);
    }
    private double calculateQuantile(List<Double> data, double quantile) {
        if (quantile < 0.0 || quantile > 1.0) {
            throw new IllegalArgumentException("Quantile must be between 0.0 and 1.0");
        }

        // Сортуємо дані
        Collections.sort(data);

        // Обчислюємо індекс квантиля
        double index = quantile * (data.size() - 1);

        // Якщо індекс ціле число, повертаємо відповідне значення
        if (index % 1 == 0) {
            return data.get((int) index);
        }

        // Якщо індекс не ціле число, інтерполюємо між двома значеннями
        int lowerIndex = (int) Math.floor(index);
        int upperIndex = (int) Math.ceil(index);
        double lowerValue = data.get(lowerIndex);
        double upperValue = data.get(upperIndex);
        return lowerValue + (upperValue - lowerValue) * (index - lowerIndex);
    }
    private double calculateCorrelationFromSales(List<Sale> sales) {
        // Створення списку сум продаж
        List<Double> amounts = sales.stream()
                .map(sale -> (double) sale.getAmount() / 100)
                .collect(Collectors.toList());

        // Створення мапи кількостей книг для кожного продажу
        Map<Integer, Integer> saleQuantitiesMap = sales.stream()
                .collect(Collectors.toMap(
                        Sale::getId,
                        sale -> sale.getSaleBooks().stream().mapToInt(SaleBook::getQuantity).sum()
                ));

        // Створення списку кількостей на основі мапи
        List<Integer> quantities = sales.stream()
                .map(sale -> saleQuantitiesMap.get(sale.getId()))
                .collect(Collectors.toList());

        return calculateCorrelation(amounts, quantities);
    }

    private double calculateCorrelation(List<Double> amounts, List<Integer> quantities) {
        if (amounts.size() != quantities.size()) {
            throw new IllegalArgumentException("The size of both lists must be the same.");
        }

        int n = amounts.size();
        double meanAmounts = calculateMean(amounts);
        double meanQuantities = quantities.stream().mapToDouble(Integer::doubleValue).average().orElse(0.0);

        double sumAmountQuantity = 0.0;
        double sumAmountSquared = 0.0;
        double sumQuantitySquared = 0.0;

        for (int i = 0; i < n; i++) {
            double amountDiff = amounts.get(i) - meanAmounts;
            double quantityDiff = quantities.get(i) - meanQuantities;

            sumAmountQuantity += amountDiff * quantityDiff;
            sumAmountSquared += amountDiff * amountDiff;
            sumQuantitySquared += quantityDiff * quantityDiff;
        }

        return sumAmountQuantity / Math.sqrt(sumAmountSquared * sumQuantitySquared);
    }
}
