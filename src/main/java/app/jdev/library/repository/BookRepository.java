package app.jdev.library.repository;

import app.jdev.library.entity.Book;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface BookRepository extends CrudRepository<Book, String>, PagingAndSortingRepository<Book, String> {

    List<Book> findAllByOrderByTitleAsc();

    List<Book> findAllByTitleIgnoreCase(String title);

    List<Book> findAllByTitleContainingIgnoreCaseOrderByTitleAsc(String title);

    List<Book> findAllByAuthor_Name(String authorName);

    List<Book> findAllByPublisher_Name(String publisherName);

    List<Book> findAllByPublisherIsNull();

    boolean existsByTitleIgnoreCase(String title);

}
