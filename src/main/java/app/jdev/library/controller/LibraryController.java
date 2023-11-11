package app.jdev.library.controller;

import app.jdev.library.entity.Author;
import app.jdev.library.entity.Book;
import app.jdev.library.entity.Publisher;
import app.jdev.library.model.SearchForm;
import app.jdev.library.service.AuthorService;
import app.jdev.library.service.BookService;
import app.jdev.library.service.PublisherService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Controller
@RequestMapping("/library")
public class LibraryController {

    private final AuthorService authorService;
    private final PublisherService publisherService;
    private final BookService bookService;

    public LibraryController(AuthorService authorService, PublisherService publisherService, BookService bookService) {
        this.authorService = authorService;
        this.publisherService = publisherService;
        this.bookService = bookService;
    }

    @GetMapping("")
    public String requestHome(Model model) {
        model.addAttribute("action", Action.HOME.getAction());
        model.addAttribute("searchForm", new SearchForm(Action.HOME.getAction(), ""));
        return "home";
    }

    @GetMapping("/authors")
    public String requestAuthors(@RequestParam(name = "name", required = false) String name, Model model) {
        List<Author> authors;

        if (name == null) {
            authors = authorService.findAllAuthors();
        } else {
            authors = authorService.findAllAuthorsByName(name);
        }

        model.addAttribute("action", Action.AUTHORS.getAction());
        model.addAttribute("searchForm", new SearchForm(Action.AUTHORS.getAction(), ""));
        model.addAttribute("authors", authors);
        return "home";
    }

    @GetMapping("/publishers")
    public String requestPublishers(@RequestParam(name = "name", required = false) String name, Model model) {
        List<Publisher> publishers;

        if (name == null) {
            publishers = publisherService.findAllPublishers();
        } else {
            publishers = publisherService.findAllPublishersByName(name);
        }

        model.addAttribute("action", Action.PUBLISHERS.getAction());
        model.addAttribute("searchForm", new SearchForm(Action.PUBLISHERS.getAction(), ""));
        model.addAttribute("publishers", publishers);
        return "home";
    }

    @GetMapping("/books")
    public String requestBooks(@RequestParam(name = "title", required = false) String title, Model model) {
        List<Book> books;

        if (title == null) {
            books = bookService.findAllBooks();
        } else {
            books = bookService.findAllBooksByTitle(title);
        }

        model.addAttribute("action", Action.BOOKS.getAction());
        model.addAttribute("searchForm", new SearchForm(Action.BOOKS.getAction(), ""));
        model.addAttribute("books", books);
        return "home";
    }

    @GetMapping("/search")
    public String search(SearchForm searchForm) {
        if (searchForm.action() == null) {
            return "redirect:/library";
        }

        String redirectURL = "";

        switch (searchForm.action()) {
            case "Home", "Books" -> redirectURL = UriComponentsBuilder.fromPath("/library/books")
                    .queryParam("title", searchForm.searchText())
                    .build().toUriString();
            case "Publishers" -> redirectURL = UriComponentsBuilder.fromPath("/library/publishers")
                    .queryParam("name", searchForm.searchText())
                    .build().toUriString();
            case "Authors" -> redirectURL = UriComponentsBuilder.fromPath("/library/authors")
                    .queryParam("name", searchForm.searchText())
                    .build().toUriString();
        }

        return "redirect:" + redirectURL;
    }

    @GetMapping("/new/author")
    public String requestNewAuthor(Model model) {
        model.addAttribute("action", Action.REGISTER.getAction());
        model.addAttribute("searchForm", new SearchForm(Action.BOOKS.getAction(), ""));
        model.addAttribute("author", new Author());
        return "new-author";
    }

    @PostMapping("/new/author")
    public String registerAuthor(Author author, Model model) {
        model.addAttribute("action", Action.REGISTER.getAction());
        model.addAttribute("searchForm", new SearchForm(Action.BOOKS.getAction(), ""));
        author.setName(author.getName().trim());

        if (authorService.existsAuthorByName(author.getName())) {
            model.addAttribute("error", "error");
        } else {
            authorService.saveAuthor(author);
            model.addAttribute("success", "success");
            author.setName("");
        }

        return "new-author";
    }

    @GetMapping("/new/publisher")
    public String requestNewPublisher(Model model) {
        model.addAttribute("action", Action.REGISTER.getAction());
        model.addAttribute("searchForm", new SearchForm(Action.BOOKS.getAction(), ""));
        model.addAttribute("publisher", new Publisher());
        return "new-publisher";
    }

    @PostMapping("/new/publisher")
    public String registerPublisher(Publisher publisher, Model model) {
        model.addAttribute("action", Action.REGISTER.getAction());
        model.addAttribute("searchForm", new SearchForm(Action.BOOKS.getAction(), ""));
        publisher.setName(publisher.getName().trim());

        if (publisherService.existsPublisherByName(publisher.getName())) {
            model.addAttribute("error", "error");
        } else {
            publisherService.savePublisher(publisher);
            model.addAttribute("success", "success");
            publisher.setName("");
        }

        return "new-publisher";
    }

}
