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

import app.jdev.library.entity.Author;
import app.jdev.library.model.SearchForm;
import app.jdev.library.service.AuthorService;
import app.jdev.library.service.BookService;
import app.jdev.library.service.LogService;
import jakarta.websocket.server.PathParam;

@Controller
@RequestMapping("/authors")
public class AuthorController {

    private final AuthorService authorService;
    private final BookService bookService;

    public AuthorController(AuthorService authorService, BookService bookService) {
        this.authorService = authorService;
        this.bookService = bookService;
    }

    @GetMapping("")
    public String requestAuthors(@RequestParam(name = "name", required = false) String name, Model model) {
        List<Author> authors;
        String url = "/authors";

        if (name == null) {
            authors = authorService.findAllAuthors();
        } else {
            authors = authorService.findAllAuthorsByName(name);
            url += "?name=" + UriComponentsBuilder.fromUriString(name).build().toUriString();
        }

        model.addAttribute("action", Action.AUTHORS.getAction());
        model.addAttribute("searchForm", new SearchForm(Action.AUTHORS.getAction(), ""));
        model.addAttribute("authors", authors);
        model.addAttribute("url", url);
        LogService.log(Action.AUTHORS, "Request authors");
        return "home";
    }

    @GetMapping("/new")
    public String requestNewAuthor(Model model) {
        model.addAttribute("action", Action.REGISTER.getAction());
        model.addAttribute("searchForm", new SearchForm(Action.AUTHORS.getAction(), ""));
        model.addAttribute("author", new Author());
        LogService.log(Action.REGISTER, "Request new author");
        return "new-edit-author";
    }

    @PostMapping("/new")
    public String registerAuthor(Author author, Model model) {
        model.addAttribute("action", Action.REGISTER.getAction());
        model.addAttribute("searchForm", new SearchForm(Action.AUTHORS.getAction(), ""));
        author.setName(author.getName().trim());

        if (author.getName().isBlank()) {
            model.addAttribute("error", "The name is invalid!");
        } else if (authorService.existsAuthorByName(author.getName())) {
            model.addAttribute("error", "The author already exists!");
        } else {
            if (authorService.existsDisabledAuthorByName(author.getName())) {
                Author savedAuthor = authorService.findDisabledAuthorByName(author.getName());
                savedAuthor.setEnabled(true);
                savedAuthor.setName(author.getName());
                author = savedAuthor;
            }

            authorService.saveAuthor(author);
            model.addAttribute("success", "Author successfully registered.");
            LogService.log(Action.REGISTER, "Register author - Author = " + author);
            author.setName("");
        }

        return "new-edit-author";
    }

    @GetMapping("/edit")
    public String requestAuthorEdit(@RequestParam(name = "id") Long id, Model model) {
        model.addAttribute("action", Action.EDIT.getAction());
        model.addAttribute("searchForm", new SearchForm(Action.AUTHORS.getAction(), ""));

        var author = new Author();
        try {
            author = authorService.findAuthorById(id);
        } catch (NoSuchElementException e) {
            model.addAttribute("error", e.getMessage());
        }

        model.addAttribute("author", author);
        LogService.log(Action.EDIT, "Edit author - Author ID = " + id);
        return "new-edit-author";
    }

    @PutMapping("/edit")
    public String updateAuthor(Author author, Model model) {
        model.addAttribute("action", Action.EDIT.getAction());
        model.addAttribute("searchForm", new SearchForm(Action.AUTHORS.getAction(), ""));

        if (author.getName().isBlank()) {
            model.addAttribute("error", "The name is invalid!");
        } else if (authorService.existsAuthorByName(author.getName())) {
            model.addAttribute("error", "The author already exists!");
        } else {
            authorService.saveAuthor(author);
            model.addAttribute("success", "Author updated.");
            LogService.log(Action.EDIT, "Update author - Author ID = " + author.getId());
        }

        return "new-edit-author";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteAuthor(@PathParam(value = "id") Long id, String url) {
        Author author = authorService.findAuthorById(id);
        bookService.findAllBooksByAuthorName(author.getName()).forEach(bookService::deleteBook);
        authorService.deleteAuthor(author);
        LogService.log(Action.DELETE, "Delete author - Author ID = " + author.getId());
        return "redirect:" + url;
    }

}
