package com.example.bookstore_app.contoller;

import com.example.bookstore_app.entity.Book;
import com.example.bookstore_app.service.AnalitycsService;
import com.example.bookstore_app.utils.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/analitycs")
//@CrossOrigin(origins = "http://localhost:8083") // Вказати конкретний URL клієнтського додатку
@RequiredArgsConstructor
public class AnalitycsController {
    @Autowired
    private final AnalitycsService analitycsService;

    @GetMapping("/trends")
    public ResponseEntity<?> getTrends() {
        try {
            return ResponseEntity.ok(analitycsService.forecastDemandTrends());

        } catch (IllegalArgumentException FormatException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Fields have incorrect values");
        } catch (CustomException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        }
    }
    @GetMapping("/arima")
    public ResponseEntity<?> getArima() {
        try {
            return ResponseEntity.ok(analitycsService.forecastDemandTrendsARIMA());

        } catch (IllegalArgumentException FormatException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Fields have incorrect values");
        } catch (CustomException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        }
    }
    @GetMapping("/arima-excel")
    public ResponseEntity<byte[]> getArimaExcel() {
        try {
            Map<Book, Integer> forecastDemand = analitycsService.forecastDemandTrends();
            Map<Book, Double> forecastDemandTrendsARIMA = analitycsService.forecastDemandTrendsARIMA();

            ByteArrayOutputStream outputStream = analitycsService.generateForecastDemandExcel(forecastDemandTrendsARIMA, forecastDemand);
            HttpHeaders headers = createHeaderExcel("genre_report.xlsx");

            return new ResponseEntity<>(outputStream.toByteArray(), headers, HttpStatus.OK);

        } catch (CustomException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getMessage().getBytes());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage().getBytes());
        }
    }
    public HttpHeaders createHeaderExcel(String file){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
//        headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
        headers.setContentDispositionFormData("attachment", file);
        return headers;
    }
}
