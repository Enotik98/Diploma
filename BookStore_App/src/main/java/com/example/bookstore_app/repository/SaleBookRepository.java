package com.example.bookstore_app.repository;

import com.example.bookstore_app.entity.SaleBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SaleBookRepository extends JpaRepository<SaleBook, Integer> {

}