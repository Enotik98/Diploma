package com.example.bookstore_app.entity;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@Table(name = "books", schema = "public")
@Entity(name = "books")
@NoArgsConstructor
@Getter
@Setter
public class Book {
    @Id
    @Column(name = "id", updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "title")
    private String title;
    @Column(name = "author")
    private String author;
    @Column(name = "pub_year")
    private int pub_year;
    @Column(name = "price")
    private int price ;
    @Column(name = "quantity")
    private int quantity;
    @Column(name = "isbn")
    private String ISBN;
    @Column(name = "about")
    private String about ; //type for text
    @Column(name = "path_img")
    private String path_img;
    @ManyToMany
    @JoinTable(
            name = "books_genres",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private List<Genre> genres;
}
