package app.jdev.library.repository;

import app.jdev.library.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuthorRepository extends JpaRepository<Author, Long> {

    List<Author> findAllByOrderByNameAsc();

    List<Author> findAllByNameContainingIgnoreCase(String name);

    boolean existsByNameIgnoreCase(String name);

}
