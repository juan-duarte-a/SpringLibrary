package app.jdev.library.model;

public record BookForm(String isbn,
                       String title,
                       String authorName,
                       String publisherName,
                       Integer year) {
}
