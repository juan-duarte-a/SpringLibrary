package app.jdev.library.repository;

import app.jdev.library.entity.Author;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository extends CrudRepository<Author, Long> {

    List<Author> findAllByEnabledTrueOrderByNameAsc();

    List<Author> findAllByNameContainingIgnoreCaseAndEnabledTrue(String name);

    Optional<Author> findByNameIgnoreCaseAndEnabledTrue(String name);

    Optional<Author> findByNameIgnoreCaseAndEnabledFalse(String name);

    boolean existsByNameIgnoreCaseAndEnabledTrue(String name);

    boolean existsByNameIgnoreCaseAndEnabledFalse(String name);

}
