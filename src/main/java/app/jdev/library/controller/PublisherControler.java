package app.jdev.library.controller;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriComponentsBuilder;

import app.jdev.library.entity.Publisher;
import app.jdev.library.model.SearchForm;
import app.jdev.library.service.BookService;
import app.jdev.library.service.LogService;
import app.jdev.library.service.PublisherService;
import jakarta.websocket.server.PathParam;

@Controller
@RequestMapping("/publishers")
public class PublisherControler {

    private final PublisherService publisherService;
    private final BookService bookService;

    public PublisherControler(PublisherService publisherService, BookService bookService) {
        this.publisherService = publisherService;
        this.bookService = bookService;
    }
    
    @GetMapping("")
    public String requestPublishers(@RequestParam(name = "name", required = false) String name, Model model) {
        List<Publisher> publishers;
        String url = "/publishers";

        if (name == null) {
            publishers = publisherService.findAllPublishers();
        } else {
            publishers = publisherService.findAllPublishersByName(name);
            url += "?name=" + UriComponentsBuilder.fromUriString(name).build().toUriString();
        }

        model.addAttribute("action", Action.PUBLISHERS.getAction());
        model.addAttribute("searchForm", new SearchForm(Action.PUBLISHERS.getAction(), ""));
        model.addAttribute("publishers", publishers);
        model.addAttribute("url", url);
        LogService.log(Action.PUBLISHERS, "Request publishers");
        return "home";
    }
    
    @GetMapping("/new")
    public String requestNewPublisher(Model model) {
        model.addAttribute("action", Action.REGISTER.getAction());
        model.addAttribute("searchForm", new SearchForm(Action.PUBLISHERS.getAction(), ""));
        model.addAttribute("publisher", new Publisher());
        LogService.log(Action.REGISTER, "Request new publisher");
        return "new-edit-publisher";
    }

    @PostMapping("/new")
    public String registerPublisher(Publisher publisher, Model model) {
        model.addAttribute("action", Action.REGISTER.getAction());
        model.addAttribute("searchForm", new SearchForm(Action.PUBLISHERS.getAction(), ""));
        publisher.setName(publisher.getName().trim());

        if (publisher.getName().isBlank()) {
            model.addAttribute("error", "The name is invalid!");
        } else if (publisherService.existsPublisherByName(publisher.getName())) {
            model.addAttribute("error", "The publisher already exists!");
        }
        else {
            if (publisherService.existsDisabledPublisherByName(publisher.getName())) {
                Publisher savedPublisher = publisherService.findDisabledPublisherByName(publisher.getName());
                savedPublisher.setEnabled(true);
                savedPublisher.setName(publisher.getName());
                publisher = savedPublisher;
            }
            
            publisherService.savePublisher(publisher);
            model.addAttribute("success", "Publisher successfully registered.");
            LogService.log(Action.REGISTER, "Register publisher - Publisher = " + publisher);
            publisher.setName("");
        }

        return "new-edit-publisher";
    }

    @GetMapping("/edit")
    public String requestPublisherEdit(@RequestParam(name = "id") Long id, Model model){
        model.addAttribute("action", Action.EDIT.getAction());
        model.addAttribute("searchForm", new SearchForm(Action.PUBLISHERS.getAction(), ""));

        var publisher = new Publisher();
        try {
            publisher = publisherService.findPublisherById(id);
        } catch (NoSuchElementException e) {
            model.addAttribute("error", e.getMessage());
        }

        model.addAttribute("publisher", publisher);
        LogService.log(Action.EDIT, "Edit publisher - Publisher ID = " + id);
        return "new-edit-publisher";
    }

    @PutMapping("/edit")
    public String updatePublisher(Publisher publisher, Model model) {
        model.addAttribute("action", Action.EDIT.getAction());
        model.addAttribute("searchForm", new SearchForm(Action.PUBLISHERS.getAction(), ""));

        if (publisher.getName().isBlank()) {
            model.addAttribute("error", "The name is invalid!");
        } else if (publisherService.existsPublisherByName(publisher.getName())) {
            model.addAttribute("error", "The publisher already exists!");
        } else {
            publisherService.savePublisher(publisher);
            model.addAttribute("success", "Publisher updated.");
            LogService.log(Action.EDIT, "Update publisher - Publisher ID = " + publisher.getId());
        }

        return "new-edit-publisher";
    }

    @DeleteMapping("/delete/{id}")
    public String deletePublisher(@PathParam(value = "id") Long id, String url) {
        Publisher publisher = publisherService.findPublisherById(id);
        bookService.findAllBooksByPublisherName(publisher.getName()).forEach(bookService::deleteBook);
        publisherService.deletePublisher(publisher);
        LogService.log(Action.DELETE, "Delete publisher - Publisher ID = " + publisher.getId());
        return "redirect:" + url;
    }

}