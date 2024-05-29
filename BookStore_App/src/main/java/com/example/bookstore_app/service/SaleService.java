package com.example.bookstore_app.service;

import com.example.bookstore_app.dto.SaleBookDTO;
import com.example.bookstore_app.dto.SaleDTO;
import com.example.bookstore_app.dto.SaleOnlineDTO;
import com.example.bookstore_app.entity.*;
import com.example.bookstore_app.repository.BookRepository;
import com.example.bookstore_app.repository.SaleBookRepository;
import com.example.bookstore_app.repository.SaleRepository;
import com.example.bookstore_app.repository.UserRepository;
import com.example.bookstore_app.utils.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class SaleService {
    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private SaleBookRepository saleBookRepository;
    public List<SaleDTO> getOrders() {
        List<Sale> orders = saleRepository.findSalesByStatus(OrderStatus.CREATED);
        List<SaleDTO> ordersDto = new ArrayList<>();
        for (Sale order: orders) {
            ordersDto.add(SaleDTO.toDto(order, order.getUser()));
        }
        return ordersDto;
    }
    public SaleDTO getBasket (Integer userId) {
        try {
            User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, "User not found"));
            List<Sale> baskets = saleRepository.findByUserAndStatus(user, OrderStatus.BASKET);
            if (baskets.isEmpty()) {
                return null;
            }else {
                return SaleDTO.toDto(baskets.get(0), user);
            }

        } catch (CustomException customException) {
            throw new CustomException(customException.getStatus(), customException.getMessage());
        } catch (Exception e) {
            throw new CustomException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    public List<SaleDTO> getOrderUser (Integer userId) {
        try {
            User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, "User not found"));
            List<Sale> orders = saleRepository.findByUserAndTypeSaleAndStatusNot(user, SalesType.ONLINE, OrderStatus.BASKET);
            if (orders.isEmpty()) {
                return null;
            }else {
                List<SaleDTO> ordersDto = new ArrayList<>();
                for (Sale order: orders) {
                    ordersDto.add(SaleDTO.toDto(order, order.getUser()));
                }
                return ordersDto;
            }
        } catch (CustomException customException) {
            throw new CustomException(customException.getStatus(), customException.getMessage());
        } catch (Exception e) {
            throw new CustomException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @Transactional
    public void addBasket (SaleBookDTO saleBookDTO, Integer id) {
        try {
            User user = userRepository.findById(id).orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, "User not found with id: " + id));
            List<Sale> baskets = saleRepository.findByUserAndStatus(user, OrderStatus.BASKET);
            if (baskets.isEmpty()) {
                Sale basket = new Sale();
                basket.setUser(user);
                basket.setStatus(OrderStatus.BASKET);
                basket.setTypeSale(SalesType.ONLINE);
                basket.setAmount(0);
                basket.setSaleBooks(new ArrayList<>());
                Sale savBasket = saleRepository.save(basket);
                basket.setId(savBasket.getId());
                baskets.add(basket);
            }
            Sale basket = baskets.get(0);
            Book book = bookRepository.findById(saleBookDTO.getBookId()).orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, "Book not found with id: " + id));
            if (book.getQuantity() < saleBookDTO.getQuantity()) {
                throw new CustomException(HttpStatus.BAD_REQUEST, "Not enough stock for book");
            }
            Optional<SaleBook> existingSaleBook = basket.getSaleBooks().stream()
                    .filter(sb -> sb.getBook().getId() == saleBookDTO.getBookId())
                    .findFirst();

            SaleBook saleBook;
            if (existingSaleBook.isPresent()) {
                saleBook = existingSaleBook.get();
                int newQuantity = saleBook.getQuantity() + saleBookDTO.getQuantity();
                if (book.getQuantity() < newQuantity) {
                    throw new CustomException(HttpStatus.BAD_REQUEST, "Not enough stock for book");
                }
                saleBook.setQuantity(newQuantity);
                basket.setAmount(basket.getAmount() + book.getPrice() * saleBookDTO.getQuantity());
            }else {
                saleBook = new SaleBook();
                saleBook.setBook(book);
                saleBook.setQuantity(saleBookDTO.getQuantity());
                basket.getSaleBooks().add(saleBook);
                basket.setAmount(basket.getAmount() + saleBook.getBook().getPrice() * saleBook.getQuantity());
                saleBook.setSale(basket);
            }
            saleBookRepository.save(saleBook);
            saleRepository.save(basket);

        } catch (CustomException customException) {
            throw new CustomException(customException.getStatus(), customException.getMessage());
        } catch (Exception e) {
            throw new CustomException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
    public void createOnlineOrder (SaleOnlineDTO saleOnlineDTO, Integer id) {
        try {
            Sale sale = saleRepository.findById(saleOnlineDTO.getId()).orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, "Order not found"));
            User user = userRepository.findById(id).orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, "User not found with id: " + id));
            sale.setUser(user);
            sale.setOrderDate(new Date());
            sale.setStatus(OrderStatus.CREATED);
            saleRepository.save(sale);
        } catch (CustomException customException) {
            throw new CustomException(customException.getStatus(), customException.getMessage());
        } catch (Exception e) {
            throw new CustomException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @Transactional
    public void createShopSale (List<SaleBookDTO> saleBookDTOList, Integer seller) {
        try {
            User user = userRepository.findById(seller).orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, "Seller not found"));
            Sale sale = new Sale();
            sale.setTypeSale(SalesType.SHOP);
            sale.setStatus(OrderStatus.COMPLETED);
            sale.setSeller(user.getEmail());
            sale.setSaleDate(new Date());
            Sale saveSale = saleRepository.save(sale);

            int amount = 0;

            for(SaleBookDTO saleBookDTO: saleBookDTOList) {
                Book book = bookRepository.findById(saleBookDTO.getBookId()).orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, "Book not found with id: " + saleBookDTO.getBookId()));
                if (book.getQuantity() < saleBookDTO.getQuantity()) {
                    throw new CustomException(HttpStatus.BAD_REQUEST, "Not enough stock for book with id: " + saleBookDTO.getBookId());
                }
                book.setQuantity(book.getQuantity() - saleBookDTO.getQuantity());
                bookRepository.save(book);

                amount += book.getPrice() * saleBookDTO.getQuantity();

                SaleBook saleBook = new SaleBook();
                saleBook.setBook(book);
                saleBook.setSale(saveSale);
                saleBook.setQuantity(saleBookDTO.getQuantity());
                saleBookRepository.save(saleBook);
            }

            saveSale.setAmount(amount);
            saleRepository.save(sale);

        } catch (CustomException customException) {
            throw new CustomException(customException.getStatus(), customException.getMessage());
        } catch (Exception e) {
            throw new CustomException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
    public void finishOrder (SaleOnlineDTO saleOnlineDTO, Integer id) {
        try {
            //need add change book quantity !
            Sale sale = saleRepository.findById(saleOnlineDTO.getId()).orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, "Order not found"));
            User user = userRepository.findById(id).orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, "User not found with id: " + id));
            sale.setSaleDate(new Date());
            sale.setSeller(user.getEmail());
            sale.setStatus(OrderStatus.COMPLETED);
            saleRepository.save(sale);
        } catch (CustomException customException) {
            throw new CustomException(customException.getStatus(), customException.getMessage());
        } catch (Exception e) {
            throw new CustomException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }


}
