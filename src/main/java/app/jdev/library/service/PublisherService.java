package app.jdev.library.service;

import app.jdev.library.entity.Publisher;
import app.jdev.library.repository.PublisherRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class PublisherService {

    private final PublisherRepository publisherRepository;

    public PublisherService(PublisherRepository publisherRepository) {
        this.publisherRepository = publisherRepository;
    }

    public List<Publisher> findAllPublishers() {
        return publisherRepository.findAllByOrderByNameAsc();
    }

    public Publisher findPublisherById(Long id) {
        return publisherRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Publisher not found!"));
    }

    public List<Publisher> findAllPublishersByName(String name) {
        return publisherRepository.findAllByNameContainingIgnoreCase(name);
    }

    public boolean existsPublisherByName(String name) {
        return publisherRepository.existsByNameIgnoreCase(name);
    }

    public void savePublisher(Publisher publisher) {
        publisherRepository.save(publisher);
    }

}
