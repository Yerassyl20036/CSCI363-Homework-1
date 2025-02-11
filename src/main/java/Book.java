import java.util.ArrayList;
import java.util.List;

public class Book {
    private String title;
    private String author;
    private String isbn;
    private boolean isAvailable;
    private String condition;
    private List<String> borrowHistory;

    public Book(String title, String author, String isbn, String condition) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.isAvailable = true;
        this.condition = condition;
        this.borrowHistory = new ArrayList<>();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) { this.title = title; }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) { this.author = author; }

    public String getISBN() {
        return isbn;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) { this.condition = condition; }

    public void borrow(String userName) {
        if (isAvailable) {
            this.isAvailable = false;
            borrowHistory.add(userName);
        }
    }

    public void returnBook() {
        this.isAvailable = true;
    }

    public List<String> getBorrowHistory() {
        return borrowHistory;
    }
}
