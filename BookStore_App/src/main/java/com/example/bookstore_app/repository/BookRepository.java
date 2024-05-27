package com.example.bookstore_app.repository;

import com.example.bookstore_app.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Integer> {
//    Book findById(int id);
    Book findByTitle(String title);

//    List<Book> findAllByQuantity(int quantity);

}
