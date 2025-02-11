import java.util.ArrayList;
import java.util.List;

public class User {
    private String username;
    private String password;
    private String role;
    private int maxBorrowLimit;
    private List<Book> borrowedBooks;

    public User(String username, String password, String role, int maxBorrowLimit) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.maxBorrowLimit = maxBorrowLimit;
        this.borrowedBooks = new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }

    public boolean authenticate(String password) {
        return this.password.equals(password);
    }

    public void changePassword(String newPassword) {
        this.password = newPassword;
    }

    public boolean borrowBook(Book book) {
        if (borrowedBooks.size() < maxBorrowLimit && book.isAvailable()) {
            book.borrow(username);
            borrowedBooks.add(book);
            return true;
        }
        return false;
    }

    public boolean returnBook(Book book) {
        if (borrowedBooks.contains(book)) {
            book.returnBook();
            borrowedBooks.remove(book);
            return true;
        }
        return false;
    }

    public List<Book> getBorrowedBooks() {
        return borrowedBooks;
    }
}
