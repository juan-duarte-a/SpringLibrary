package app.jdev.library.entity;

import jakarta.persistence.*;

@Entity
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String isbn;

    @Column(nullable = false, unique = true)
    private String title;

    private Integer publicationYear;

    private int copies;

    private int borrowedCopies;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Author author;

    @ManyToOne
    private Publisher publisher;

    @Column(nullable = false)
    private boolean enabled;

    public Book() {
        this.enabled = true;
    }

    public Book(String title, Integer publicationYear, Author author, Publisher publisher) {
        this();
        this.title = title;
        this.publicationYear = publicationYear;
        this.author = author;
        this.publisher = publisher;
    }

    public Book(String isbn, String title, Integer publicationYear, Author author, Publisher publisher) {
        this();
        this.isbn = isbn;
        this.title = title;
        this.publicationYear = publicationYear;
        this.author = author;
        this.publisher = publisher;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(Integer publicationYear) {
        this.publicationYear = publicationYear;
    }

    public int getCopies() {
        return copies;
    }

    public void setCopies(int copies) {
        this.copies = copies;
    }

    public int getAvailableCopies() {
        return copies - borrowedCopies;
    }

    public int getBorrowedCopies() {
        return borrowedCopies;
    }

    public void setBorrowedCopies(int borrowedCopies) {
        this.borrowedCopies = borrowedCopies;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public String toString() {
        return "Book:\n"
                + " - Title: " + title + "\n"
                + " - ISBN: " + isbn + "\n"
                + " - Author: " + author + "\n"
                + " - Title: " + publisher
                + ". " + publicationYear + "\n"
                + " - Copies: " + copies
                + ". Borrowed: " + borrowedCopies
                + ". Available: " + getAvailableCopies();
    }

}
