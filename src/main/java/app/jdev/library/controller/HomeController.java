package app.jdev.library.controller;

import app.jdev.library.entity.Author;
import app.jdev.library.entity.Book;
import app.jdev.library.entity.Publisher;
import app.jdev.library.service.AuthorService;
import app.jdev.library.service.BookService;
import app.jdev.library.service.PublisherService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/library")
public class HomeController {

    private final AuthorService authorService;
    private final PublisherService publisherService;
    private final BookService bookService;

    public HomeController(AuthorService authorService, PublisherService publisherService, BookService bookService) {
        this.authorService = authorService;
        this.publisherService = publisherService;
        this.bookService = bookService;
    }

    @GetMapping("")
    public String requestHome(Model model) {
        model.addAttribute("action", "home");
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

        model.addAttribute("action", "Authors");
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

        model.addAttribute("action", "Publishers");
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

        model.addAttribute("action", "Books");
        model.addAttribute("books", books);
        return "home";
    }

}
