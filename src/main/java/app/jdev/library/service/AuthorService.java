package app.jdev.library.service;

import app.jdev.library.entity.Author;
import app.jdev.library.repository.AuthorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public Author findAuthorById(Long id) {
        return authorRepository.findById(id).orElseThrow();
    }

    public List<Author> findAllAuthors() {
        return authorRepository.findAllByOrderByNameAsc();
    }

    public List<Author> findAllAuthorsByName(String name) {
        return authorRepository.findAllByNameContainingIgnoreCase(name);
    }

}