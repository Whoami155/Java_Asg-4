import java.util.Objects;

public class Book implements Comparable<Book> {
    private int bookId;
    private String title;
    private String author;
    private String category;
    private boolean isIssued;

    public Book(int bookId, String title, String author, String category) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.category = category;
        this.isIssued = false;
    }

    public int getBookId() { return bookId; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getCategory() { return category; }
    public boolean isIssued() { return isIssued; }

    public void markAsIssued() { isIssued = true; }
    public void markAsReturned() { isIssued = false; }

    public void displayBookDetails() {
        System.out.printf("ID: %d | %s | %s | %s | %s%n",
                bookId, title, author, category, (isIssued ? "Issued" : "Available"));
    }

    @Override
    public int compareTo(Book other) {
        return this.title.compareToIgnoreCase(other.title);
    }

    @Override
    public String toString() {
        return bookId + "," + title + "," + author + "," + category + "," + isIssued;
    }

    public static Book fromString(String line) {
        // expected format: id,title,author,category,isIssued
        String[] parts = line.split(",", -1);
        if (parts.length < 5) return null;
        try {
            int id = Integer.parseInt(parts[0]);
            String title = parts[1];
            String author = parts[2];
            String category = parts[3];
            boolean issued = Boolean.parseBoolean(parts[4]);
            Book b = new Book(id, title, author, category);
            if (issued) b.markAsIssued();
            return b;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Book)) return false;
        Book book = (Book) o;
        return bookId == book.bookId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookId);
    }
}

