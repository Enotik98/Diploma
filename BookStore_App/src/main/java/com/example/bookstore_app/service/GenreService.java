package com.example.bookstore_app.service;

import com.example.bookstore_app.dto.GenreDTO;
import com.example.bookstore_app.entity.Genre;
import com.example.bookstore_app.repository.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GenreService {

    @Autowired
    private GenreRepository genreRepository;

    public List<Genre> getAllGenres() {
        return genreRepository.findAll();
    }

    public Optional<Genre> getGenreById(int id) {
        return genreRepository.findById(id);
    }

    public Genre createGenre(Genre genre) {
        return genreRepository.save(genre);
    }

    public Optional<Genre> updateGenre(int id, GenreDTO genreDetails) {
        return genreRepository.findById(id)
                .map(genre -> {
                    genre.setGenre(genreDetails.getGenre());
                    return genreRepository.save(genre);
                });
    }

    public void deleteGenre(int id) {
        genreRepository.deleteById(id);
    }
}
