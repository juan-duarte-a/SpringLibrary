package app.jdev.library.repository;

import app.jdev.library.entity.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PublisherRepository extends JpaRepository<Publisher, Long> {

    List<Publisher> findAllByOrderByNameAsc();

    List<Publisher> findAllByNameContainingIgnoreCase(String name);

    boolean existsByNameIgnoreCase(String name);

}
