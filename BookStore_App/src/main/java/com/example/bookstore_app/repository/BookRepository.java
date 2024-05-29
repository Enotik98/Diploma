package com.example.bookstore_app.repository;

import com.example.bookstore_app.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Integer> {
//    Book findById(int id);
    Book findByTitle(String title);
    @Query("SELECT b FROM books b WHERE b.title LIKE %:query% OR b.ISBN LIKE %:query%")
    List<Book> findByTitleOrIsbnContaining(@Param("query") String query);
//    List<Book> findAllByQuantity(int quantity);

}
