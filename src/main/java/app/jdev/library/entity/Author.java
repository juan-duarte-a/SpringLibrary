package app.jdev.library.entity;

import jakarta.persistence.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Entity
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "author")
    private List<Book> books;

    @Column(nullable = false)
    private boolean enabled;

    public Author() {
        this.enabled = true;
    }

    public Author(String name) {
        this();
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Book> getBooks() {
        return books.stream().filter(book -> book.isEnabled()).toList();
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getAuthorUriValidName() {
        return URLEncoder.encode(name, StandardCharsets.UTF_8);
    }

    @Override
    public String toString() {
        return name;
    }

}
