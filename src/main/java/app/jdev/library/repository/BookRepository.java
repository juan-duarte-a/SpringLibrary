package app.jdev.library.repository;

import app.jdev.library.entity.Book;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends CrudRepository<Book, String>, PagingAndSortingRepository<Book, String> {

    List<Book> findAllByEnabledTrueOrderByTitleAsc();

    List<Book> findAllByTitleIgnoreCaseAndEnabledTrue(String title);

    List<Book> findAllByTitleContainingIgnoreCaseAndEnabledTrueOrderByTitleAsc(String title);

    List<Book> findAllByEnabledTrueAndAuthor_Name(String authorName);

    List<Book> findAllByEnabledTrueAndPublisher_Name(String publisherName);

    List<Book> findAllByEnabledTrueAndPublisherIsNull();

    Optional<Book> findByTitleIgnoreCaseAndEnabledFalse(String title);

    boolean existsByTitleIgnoreCaseAndEnabledTrue(String title);

    boolean existsByTitleIgnoreCaseAndEnabledFalse(String title);

}
