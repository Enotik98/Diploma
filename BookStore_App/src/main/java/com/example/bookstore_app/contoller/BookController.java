package com.example.bookstore_app.contoller;

import com.example.bookstore_app.dto.BookDTO;
import com.example.bookstore_app.dto.GenreDTO;
import com.example.bookstore_app.entity.Book;
import com.example.bookstore_app.entity.Genre;
import com.example.bookstore_app.service.BookService;
import com.example.bookstore_app.service.GenreService;
import com.example.bookstore_app.utils.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/book")
//@CrossOrigin(origins = "http://localhost:8083") // Вказати конкретний URL клієнтського додатку
@RequiredArgsConstructor
public class BookController {
    @Autowired
    private final BookService bookService;
    @Autowired
    private final GenreService genreService;
    @GetMapping("")
    public ResponseEntity<?> getAllBook() {
        try {
            return ResponseEntity.ok(bookService.getAllBooks());
        } catch (CustomException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getBook(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(bookService.getBookById(id));
        } catch (IllegalArgumentException FormatException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Fields have incorrect values");
        } catch (CustomException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        }
    }
    @GetMapping("/search")
    public List<Book> searchBooks(@RequestParam("query") String query) {
        return bookService.searchBooks(query);
    }

    @GetMapping("/genre")
    public ResponseEntity<?> getAllGenre() {
        try {
            return ResponseEntity.ok(genreService.getAllGenres());
        } catch (CustomException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> createBookWithGenres(@Valid @RequestBody BookDTO bookDTO, BindingResult bindingResult) {
        try {
            if (bindingResult.hasErrors()) {
                return ResponseEntity.badRequest().body(CustomException.bindingResultToString(bindingResult) + " Please fill correct in these fields.");
            }

            bookService.createBook(bookDTO);
            return ResponseEntity.ok("add successful");
        } catch (CustomException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        } catch (Exception e) {
            if (e instanceof HttpMessageNotReadableException){
                return ResponseEntity.badRequest().body("Invalid data type");
            }
            return ResponseEntity.badRequest().body(CustomException.bindingResultToString(bindingResult));
        }
    }

    @PostMapping("/genre/create")
    public ResponseEntity<?> createGenre(@RequestBody Genre genre) {
        try {
            genreService.createGenre(genre);
            return ResponseEntity.ok("add successful");
        } catch (IllegalArgumentException FormatException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Fields have incorrect values");
        } catch (CustomException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        }
    }
    @PutMapping("/genre")
    public ResponseEntity<?> updateGenre(@RequestBody GenreDTO genre) {
        try {
            genreService.updateGenre(genre.getId(), genre);
            return ResponseEntity.ok("add successful");
        } catch (IllegalArgumentException FormatException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Fields have incorrect values");
        } catch (CustomException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        }
    }
}
