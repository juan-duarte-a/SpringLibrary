package app.jdev.library.repository;

import app.jdev.library.entity.Publisher;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface PublisherRepository extends CrudRepository<Publisher, Long> {

    List<Publisher> findAllByEnabledTrueOrderByNameAsc();

    List<Publisher> findAllByNameContainingIgnoreCaseAndEnabledTrue(String name);

    Optional<Publisher> findByNameIgnoreCaseAndEnabledTrue(String name);

    Optional<Publisher> findByNameIgnoreCaseAndEnabledFalse(String name);

    boolean existsByNameIgnoreCaseAndEnabledTrue(String name);
    
    boolean existsByNameIgnoreCaseAndEnabledFalse(String name);

}
