package app.jdev.library.service;

import app.jdev.library.entity.Book;
import app.jdev.library.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> findAllBooks() {
        return bookRepository.findAllByOrderByTitleAsc();
    }

    public List<Book> findAllBooksByTitle(String title) {
        return bookRepository.findAllByTitleIgnoreCase(title);
    }

    public Book findBookByISBN(String isbn) {
        return bookRepository.findById(isbn).orElseThrow(
                () -> new NoSuchElementException("Book not found!"));
    }

    public List<Book> findAllBooksByTitleContaining(String title) {
        return bookRepository.findAllByTitleContainingIgnoreCaseOrderByTitleAsc(title);
    }

    public List<Book> findAllBooksByAuthorName(String authorName) {
        List<Book> books = bookRepository.findAllByAuthor_Name(authorName);
        books.sort(Comparator.comparing(Book::getTitle));
        return books;
    }

    public List<Book> findAllBooksByPublisherName(String publisherName) {
        List<Book> books = publisherName.isEmpty() ?
                findAllBooksNoPublisher() : bookRepository.findAllByPublisher_Name(publisherName);
        books.sort(Comparator.comparing(Book::getTitle));
        return books;
    }

    public List<Book> findAllBooksNoPublisher() {
        return bookRepository.findAllByPublisherIsNull();
    }

    public void saveBook(Book book) {
        bookRepository.save(book);
    }

    public boolean existsBookByTitle(String title) {
        return bookRepository.existsByTitleIgnoreCase(title);
    }

    public boolean invalidData(String title, Integer year) {
        return title.isBlank() || year < 0 || year > LocalDate.now().getYear();
    }

}
