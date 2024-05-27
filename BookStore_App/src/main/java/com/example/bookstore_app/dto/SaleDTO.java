package com.example.bookstore_app.dto;

import com.example.bookstore_app.entity.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class SaleDTO {
    private int id;
    private UserDTO user;
    private Date orderDate;
    private OrderStatus status;
    private String seller;
    private Date saleDate;
    private SalesType typeSale;
    private int amount;
    private List<SaleBook> saleBooks;

    public static SaleDTO toDto(Sale sale, User user) {
        SaleDTO dto = new SaleDTO();
        dto.setId(sale.getId());
        if (user != null) {
            dto.setUser(UserDTO.toDto(user));
        }
        dto.setOrderDate(sale.getOrderDate());
        dto.setStatus(sale.getStatus());
        dto.setSeller(sale.getSeller());
        dto.setSaleDate(sale.getSaleDate());
        dto.setTypeSale(sale.getTypeSale());
        dto.setAmount(sale.getAmount());
        List<SaleBook> saleBooks = new ArrayList<>();
        for (SaleBook sale_book: sale.getSaleBooks()) {
            SaleBook book = new SaleBook();
            book.setBook(sale_book.getBook());
            book.setQuantity(sale_book.getQuantity());
            saleBooks.add(book);
        }
        dto.setSaleBooks(saleBooks);

        return dto;
    }


}
