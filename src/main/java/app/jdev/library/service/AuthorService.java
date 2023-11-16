package app.jdev.library.service;

import app.jdev.library.entity.Author;
import app.jdev.library.repository.AuthorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public Author findAuthorById(Long id) {
        return authorRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Author not found!"));
    }

    public Author findAuthorByName(String name) {
        return authorRepository.findByNameIgnoreCaseAndEnabledTrue(name).orElseThrow(
                () -> new NoSuchElementException("Author not found!"));
    }

    public Author findDisabledAuthorByName(String name) {
        return authorRepository.findByNameIgnoreCaseAndEnabledFalse(name).orElseThrow(
                () -> new NoSuchElementException("Author not found!"));
    }

    public List<Author> findAllAuthors() {
        return authorRepository.findAllByEnabledTrueOrderByNameAsc();
    }

    public List<Author> findAllAuthorsByName(String name) {
        return authorRepository.findAllByNameContainingIgnoreCaseAndEnabledTrue(name);
    }

    public boolean existsAuthorByName(String name) {
        return authorRepository.existsByNameIgnoreCaseAndEnabledTrue(name);
    }

    public boolean existsDisabledAuthorByName(String name) {
        return authorRepository.existsByNameIgnoreCaseAndEnabledFalse(name);
    }

    public void saveAuthor(Author author) {
        authorRepository.save(author);
    }

    public void deleteAuthor(Author author) {
        author.setEnabled(false);
        authorRepository.save(author);
    }

}
