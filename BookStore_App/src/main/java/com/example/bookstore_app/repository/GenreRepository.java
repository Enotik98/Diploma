package com.example.bookstore_app.repository;

import com.example.bookstore_app.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenreRepository extends JpaRepository<Genre, Integer> {
//    Genre findById(int id);
//    Genre findByName(String name);
}
