package com.example.bookstore_app.dto;

import com.example.bookstore_app.entity.Book;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
public class BookDTO {
    private int id;
    @NotEmpty(message = "Title mustn't be empty.")
    private String title;
    @NotEmpty(message = "Author mustn't be empty.")
    private String author;
    @Min(value = 1, message = "Pub year book must between 1 and 9999.")
    @NotNull(message = "PubYear mustn't be empty.")
    private int pubYear;
    @Min(value = 1, message = "Price book must between 1 and 9999999.")
    @NotNull(message = "Price mustn't be empty.")
    private int price;
    @Min(value = 1, message = "Quantity book must between 1 and 9999.")
    @NotNull(message = "Quantity mustn't be empty.")
    private int quantity;
    @NotEmpty(message = "ISBN mustn't be empty.")
    private String isbn;
    private String about;
    private List<Integer> genres;
    public static Book toEntity(BookDTO bookDTO) {
        Book book = new Book();
        book.setId(bookDTO.getId());
        book.setTitle(bookDTO.getTitle());
        book.setAuthor(bookDTO.getAuthor());
        book.setPub_year(bookDTO.getPubYear());
        book.setPrice(bookDTO.getPrice());
        book.setQuantity(bookDTO.getQuantity());
        book.setISBN(bookDTO.getIsbn());
        book.setAbout(bookDTO.getAbout());
        return book;
    }
}
