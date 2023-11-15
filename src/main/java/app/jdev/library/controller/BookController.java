package app.jdev.library.controller;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import app.jdev.library.entity.Author;
import app.jdev.library.entity.Book;
import app.jdev.library.entity.Publisher;
import app.jdev.library.model.BookForm;
import app.jdev.library.model.SearchForm;
import app.jdev.library.service.AuthorService;
import app.jdev.library.service.BookService;
import app.jdev.library.service.LogService;
import app.jdev.library.service.PublisherService;

@Controller
@RequestMapping("/books")
public class BookController {
    
    private final BookService bookService;
    private final AuthorService authorService;
    private final PublisherService publisherService;
    
    public BookController(BookService bookService, AuthorService authorService, PublisherService publisherService) {
        this.bookService = bookService;
        this.authorService = authorService;
        this.publisherService = publisherService;
    }

    @GetMapping("")
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

    @GetMapping("/new")
    public String requestNewBook(Model model) {
        model.addAttribute("action", Action.REGISTER.getAction());
        model.addAttribute("searchForm", new SearchForm(Action.BOOKS.getAction(), ""));
        model.addAttribute("authors", authorService.findAllAuthors());
        model.addAttribute("publishers", publisherService.findAllPublishers());
        model.addAttribute("bookForm", new BookForm(null, null, null, null, null));
        LogService.log(Action.REGISTER, "Request new book");
        return "new-edit-book";
    }

    @PostMapping("/new")
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


    @GetMapping("/edit")
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
                    book.getPublicationYear()
            );
            model.addAttribute("bookForm", bookForm);
        } catch (NoSuchElementException e) {
            model.addAttribute("bookForm", new BookForm(null, null, null, null, null));
            model.addAttribute("error", e.getMessage());
        }

        LogService.log(Action.EDIT, "Edit book - Book ISBN = " + isbn);
        return "new-edit-book";
    }

    @PutMapping("/edit")
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
