package app.jdev.library.repository;

import app.jdev.library.entity.Author;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AuthorRepository extends CrudRepository<Author, Long> {

    List<Author> findAllByOrderByNameAsc();

    List<Author> findAllByNameContainingIgnoreCase(String name);

    boolean existsByNameIgnoreCase(String name);

}
