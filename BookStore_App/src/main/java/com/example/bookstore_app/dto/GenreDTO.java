package com.example.bookstore_app.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class GenreDTO {
    private int id;
    @NotEmpty(message = "Genre mustn't be empty.")
    private String genre;
}
