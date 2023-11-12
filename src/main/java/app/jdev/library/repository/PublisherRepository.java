package app.jdev.library.repository;

import app.jdev.library.entity.Publisher;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PublisherRepository extends CrudRepository<Publisher, Long> {

    List<Publisher> findAllByOrderByNameAsc();

    List<Publisher> findAllByNameContainingIgnoreCase(String name);

    boolean existsByNameIgnoreCase(String name);

}
