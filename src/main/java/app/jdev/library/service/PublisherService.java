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
        return publisherRepository.findAllByEnabledTrueOrderByNameAsc();
    }

    public Publisher findPublisherById(Long id) {
        return publisherRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Publisher not found!"));
    }

    public Publisher findPublisherByName(String name) {
        return publisherRepository.findByNameIgnoreCaseAndEnabledTrue(name).orElseThrow(
                () -> new NoSuchElementException("Publisher not found!"));
    }

    public Publisher findDisabledPublisherByName(String name) {
        return publisherRepository.findByNameIgnoreCaseAndEnabledFalse(name).orElseThrow(
                () -> new NoSuchElementException("Publisher not found!"));
    }

    public List<Publisher> findAllPublishersByName(String name) {
        return publisherRepository.findAllByNameContainingIgnoreCaseAndEnabledTrue(name);
    }

    public void savePublisher(Publisher publisher) {
        publisherRepository.save(publisher);
    }

    public void deletePublisher(Publisher publisher) {
        publisher.setEnabled(false);
        publisherRepository.save(publisher);
    }

    public boolean existsPublisherByName(String name) {
        return publisherRepository.existsByNameIgnoreCaseAndEnabledTrue(name);
    }

    public boolean existsDisabledPublisherByName(String name) {
        return publisherRepository.existsByNameIgnoreCaseAndEnabledFalse(name);
    }

}
