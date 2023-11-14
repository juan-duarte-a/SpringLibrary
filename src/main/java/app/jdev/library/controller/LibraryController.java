package app.jdev.library.controller;

import app.jdev.library.entity.Author;
import app.jdev.library.entity.Book;
import app.jdev.library.entity.Publisher;
import app.jdev.library.model.BookForm;
import app.jdev.library.model.SearchForm;
import app.jdev.library.service.AuthorService;
import app.jdev.library.service.BookService;
import app.jdev.library.service.LogService;
import app.jdev.library.service.PublisherService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.NoSuchElementException;

@Controller
@RequestMapping("/")
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
        LogService.log(Action.HOME, "Request homepage");
        return "home";
    }

    @GetMapping("authors")
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
        LogService.log(Action.AUTHORS, "Request authors");
        return "home";
    }

    @GetMapping("publishers")
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
        LogService.log(Action.PUBLISHERS, "Request publishers");
        return "home";
    }

    @GetMapping("books")
    public String requestBooks(@RequestParam(name = "title", required = false) String title,
                               @RequestParam(name = "author", required = false) String authorName,
                               @RequestParam(name = "publisher", required = false) String publisherName,
                               Model model) {
        List<Book> books;

        if (title == null && authorName == null && publisherName == null) {
            books = bookService.findAllBooks();
        } else if (title != null) {
            books = bookService.findAllBooksByTitleContaining(title);
        } else if (authorName != null) {
            books = bookService.findAllBooksByAuthorName(authorName);
            model.addAttribute("authorName", authorName);
        } else {
            books = bookService.findAllBooksByPublisherName(publisherName);
            model.addAttribute("publisherName", publisherName);
        }

        model.addAttribute("action", Action.BOOKS.getAction());
        model.addAttribute("searchForm", new SearchForm(Action.BOOKS.getAction(), ""));
        model.addAttribute("books", books);
        LogService.log(Action.BOOKS, "Request books - "
                + (title != null ? "Title = " + title
                : authorName != null ? "Author = " + authorName
                : publisherName != null ? "Publisher = " + publisherName
                : "All"));
        return "home";
    }

    @GetMapping("search")
    public String search(SearchForm searchForm) {
        if (searchForm.action() == null) {
            return "redirect:/";
        }

        String redirectURL = "";

        switch (searchForm.action()) {
            case "Home", "Books" -> redirectURL = UriComponentsBuilder.fromPath("books")
                    .queryParam("title", URLEncoder.encode(searchForm.searchText(), StandardCharsets.UTF_8))
                    .build().toUriString();
            case "Publishers" -> redirectURL = UriComponentsBuilder.fromPath("publishers")
                    .queryParam("name", URLEncoder.encode(searchForm.searchText(), StandardCharsets.UTF_8))
                    .build().toUriString();
            case "Authors" -> redirectURL = UriComponentsBuilder.fromPath("authors")
                    .queryParam("name", URLEncoder.encode(searchForm.searchText(), StandardCharsets.UTF_8))
                    .build().toUriString();
        }

        LogService.log(Action.SEARCHBAR, "Search = " + searchForm.searchText());
        return "redirect:/" + redirectURL;
    }

    @GetMapping("authors/new")
    public String requestNewAuthor(Model model) {
        model.addAttribute("action", Action.REGISTER.getAction());
        model.addAttribute("searchForm", new SearchForm(Action.AUTHORS.getAction(), ""));
        model.addAttribute("author", new Author());
        LogService.log(Action.REGISTER, "Request new author");
        return "new-edit-author";
    }

    @PostMapping("authors/new")
    public String registerAuthor(Author author, Model model) {
        model.addAttribute("action", Action.REGISTER.getAction());
        model.addAttribute("searchForm", new SearchForm(Action.AUTHORS.getAction(), ""));
        author.setName(author.getName().trim());

        if (author.getName().isBlank()) {
            model.addAttribute("error", "The name is invalid!");
        } else if (authorService.existsAuthorByName(author.getName())) {
            model.addAttribute("error", "The author already exists!");
        }
        else {
            authorService.saveAuthor(author);
            model.addAttribute("success", "Author successfully registered.");
            LogService.log(Action.REGISTER, "Register author - Author = " + author);
            author.setName("");
        }

        return "new-edit-author";
    }

    @GetMapping("publishers/new")
    public String requestNewPublisher(Model model) {
        model.addAttribute("action", Action.REGISTER.getAction());
        model.addAttribute("searchForm", new SearchForm(Action.PUBLISHERS.getAction(), ""));
        model.addAttribute("publisher", new Publisher());
        LogService.log(Action.REGISTER, "Request new publisher");
        return "new-edit-publisher";
    }

    @PostMapping("publishers/new")
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
            publisherService.savePublisher(publisher);
            model.addAttribute("success", "Publisher successfully registered.");
            LogService.log(Action.REGISTER, "Register publisher - Publisher = " + publisher);
            publisher.setName("");
        }

        return "new-edit-publisher";
    }

    @GetMapping("books/new")
    public String requestNewBook(Model model) {
        model.addAttribute("action", Action.REGISTER.getAction());
        model.addAttribute("searchForm", new SearchForm(Action.BOOKS.getAction(), ""));
        model.addAttribute("authors", authorService.findAllAuthors());
        model.addAttribute("publishers", publisherService.findAllPublishers());
        model.addAttribute("bookForm", new BookForm(null, null, null, null, null));
        LogService.log(Action.REGISTER, "Request new book");
        return "new-edit-book";
    }

    @PostMapping("books/new")
    public String registerBook(BookForm bookForm, Model model) {
        model.addAttribute("action", Action.REGISTER.getAction());
        model.addAttribute("searchForm", new SearchForm(Action.BOOKS.getAction(), ""));
        model.addAttribute("authors", authorService.findAllAuthors());
        model.addAttribute("publishers", publisherService.findAllPublishers());

        if (isValidBookForm(bookForm, model)) {
            Author author = authorService.findAllAuthorsByName(bookForm.authorName()).get(0);
            Publisher publisher = (bookForm.publisherName() == null
                    || bookForm.publisherName().equals("(no publisher)")) ?
                    null : publisherService.findAllPublishersByName(bookForm.publisherName()).get(0);

            var book = new Book(
                    bookForm.title(),
                    bookForm.year(),
                    author,
                    publisher
            );

            bookService.saveBook(book);
            model.addAttribute("success", "Book successfully registered.");
        }

        LogService.log(Action.REGISTER, "Register book - Book = " + bookForm.title());
        return "new-edit-book";
    }

    @GetMapping("authors/edit")
    public String requestAuthorEdit(@RequestParam(name = "id") Long id, Model model){
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

    @PostMapping("authors/edit")
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

    @GetMapping("publishers/edit")
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

    @PostMapping("publishers/edit")
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

    @GetMapping("books/edit")
    public String requestBookEdit(@RequestParam(name = "isbn") String isbn, Model model) {
        model.addAttribute("action", Action.EDIT.getAction());
        model.addAttribute("searchForm", new SearchForm(Action.BOOKS.getAction(), ""));
        model.addAttribute("authors", authorService.findAllAuthors());
        model.addAttribute("publishers", publisherService.findAllPublishers());

        try {
            Book book = bookService.findBookByISBN(isbn);
            var bookForm = new BookForm(
                    book.getIsbn(),
                    book.getTitle(),
                    book.getAuthor().getName(),
                    book.getPublisher() == null ? "(no publisher)" : book.getPublisher().getName(),
                    book.getYear()
            );
            model.addAttribute("bookForm", bookForm);
        } catch (NoSuchElementException e) {
            model.addAttribute("bookForm", new BookForm(null, null, null, null, null));
            model.addAttribute("error", e.getMessage());
        }

        LogService.log(Action.EDIT, "Edit book - Book ISBN = " + isbn);
        return "new-edit-book";
    }

    @PostMapping("books/edit")
    public String updateBook(BookForm bookForm, Model model) {
        model.addAttribute("action", Action.EDIT.getAction());
        model.addAttribute("searchForm", new SearchForm(Action.BOOKS.getAction(), ""));
        model.addAttribute("authors", authorService.findAllAuthors());
        model.addAttribute("publishers", publisherService.findAllPublishers());

        if (isValidBookForm(bookForm, model)) {
            Author author = authorService.findAllAuthorsByName(bookForm.authorName()).get(0);
            Publisher publisher = (bookForm.publisherName() == null
                    || bookForm.publisherName().equals("(no publisher)")) ?
                    null : publisherService.findAllPublishersByName(bookForm.publisherName()).get(0);

            var book = new Book(
                    bookForm.isbn(),
                    bookForm.title(),
                    bookForm.year(),
                    author,
                    publisher
            );

            bookService.saveBook(book);
            model.addAttribute("success", "Book successfully updated.");
            LogService.log(Action.EDIT, "Update book - Book ISBN = " + bookForm.isbn());
        }

        return "new-edit-book";
    }

    private boolean isValidBookForm(BookForm bookForm, Model model) {
        boolean valid = true;
        List<Book> books = bookService.findAllBooksByTitle(bookForm.title());

        if (!books.isEmpty() && !books.get(0).getIsbn().equals(bookForm.isbn())) {
            model.addAttribute("error", "Book already exists!");
            valid = false;
        } else if (bookService.invalidData(bookForm.title(), bookForm.year())) {
            model.addAttribute("error", "Invalid data!");
            valid = false;
        } else if (!authorService.existsAuthorByName(bookForm.authorName())) {
            model.addAttribute("error", "Author does not exist!");
            valid = false;
        } else if (bookForm.publisherName() != null && !bookForm.publisherName().equals("(no publisher)")
                && !publisherService.existsPublisherByName(bookForm.publisherName())) {
            model.addAttribute("error", "Publisher does not exist!");
            valid = false;
        }

        return valid;
    }

}
