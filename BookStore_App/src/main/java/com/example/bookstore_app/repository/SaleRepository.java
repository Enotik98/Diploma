package com.example.bookstore_app.repository;

import com.example.bookstore_app.entity.OrderStatus;
import com.example.bookstore_app.entity.Sale;
import com.example.bookstore_app.entity.SalesType;
import com.example.bookstore_app.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Integer> {
    List<Sale> findByUserAndStatus(User user, OrderStatus status);
//    @Query("SELECT * FROM Sale s WHERE s.status = 'COMPLETED::' AND s.saleDate >= :startDate")
//    List<Sale> findSalesByStatusCompletedAndLastMonth(Date startDate);
    List<Sale> findSalesByStatus(OrderStatus status);
    List<Sale> findByUserAndTypeSaleAndStatusNot(User user, SalesType salesType, OrderStatus status);
}
