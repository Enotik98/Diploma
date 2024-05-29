package com.example.bookstore_app.service;

import com.example.bookstore_app.dto.BookDTO;
import com.example.bookstore_app.entity.Book;
import com.example.bookstore_app.entity.Genre;
import com.example.bookstore_app.repository.BookRepository;
import com.example.bookstore_app.repository.GenreRepository;
import com.example.bookstore_app.utils.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private GenreRepository genreRepository;

    public List<Book> getAllBooks() {
        try {
            return bookRepository.findAll();
        } catch (Exception e) {
            throw new CustomException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    public Optional<Book> getBookById(int id) {
        try {
            return bookRepository.findById(id);
        }  catch (CustomException customException) {
            throw new CustomException(customException.getStatus(), customException.getMessage());
        } catch (Exception e) {
            throw new CustomException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
    public List<Book> searchBooks(String query) {
        return bookRepository.findByTitleOrIsbnContaining(query);
    }

    public void createBook(BookDTO bookDTO) {
        try {
            Book exBook = bookRepository.findByTitle(bookDTO.getTitle());
            if (exBook != null) {
                throw new CustomException(HttpStatus.CONFLICT, "A book with this title already exists");
            }
            Book book = BookDTO.toEntity(bookDTO);
            book.setPrice(bookDTO.getPrice() * 100);

            // Збереження книги у базі даних
            Book savedBook = bookRepository.save(book);


            // Оновлення жанрів у книзі
            List<Genre> genres = bookDTO.getGenres().stream()
                    .map(genreId -> genreRepository.findById(genreId).orElse(null))
                    .collect(Collectors.toList());
            savedBook.setGenres(genres);
            bookRepository.save(savedBook);
        } catch (CustomException customException) {
            throw new CustomException(customException.getStatus(), customException.getMessage());
        } catch (Exception e) {
            throw new CustomException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
