package com.example.bookstore_app.dto;

import com.example.bookstore_app.entity.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class SaleOnlineDTO {

    private int id;
    private Date orderDate;
    private OrderStatus status;
    private String seller;
    private Date saleDate;
    private SalesType typeSale;
    private int amount;
    private List<SaleBookDTO> books;

    public static Sale toEntity(SaleOnlineDTO saleOnlineDTO) {
        Sale sale = new Sale();
        sale.setId(saleOnlineDTO.id);
        sale.setOrderDate(saleOnlineDTO.orderDate);
        return sale;
    }
}
