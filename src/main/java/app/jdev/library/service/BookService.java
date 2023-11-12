package app.jdev.library.service;

import app.jdev.library.entity.Book;
import app.jdev.library.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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
        return bookRepository.findAllByTitleContainingIgnoreCase(title);
    }

    public List<Book> findAllBooksByAuthorName(String authorName) {
        return bookRepository.findAllByAuthor_Name(authorName);
    }

    public List<Book> findAllBooksByPublisherName(String publisherName) {
        return bookRepository.findAllByPublisher_Name(publisherName);
    }

}
