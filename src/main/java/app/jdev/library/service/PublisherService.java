package app.jdev.library.service;

import app.jdev.library.entity.Publisher;
import app.jdev.library.repository.PublisherRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PublisherService {

    private final PublisherRepository publisherRepository;

    public PublisherService(PublisherRepository publisherRepository) {
        this.publisherRepository = publisherRepository;
    }

    public List<Publisher> findAllPublishers() {
        return publisherRepository.findAllByOrderByNameAsc();
    }

    public List<Publisher> findAllPublishersByName(String name) {
        return publisherRepository.findAllByNameContainingIgnoreCase(name);
    }

}
