package app.jdev.library.repository;

import app.jdev.library.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, String> {

    List<Book> findAllByOrderByTitleAsc();

    List<Book> findAllByTitleContainingIgnoreCase(String title);

}
