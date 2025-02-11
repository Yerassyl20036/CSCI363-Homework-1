import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserTest {
    private User user;

    @BeforeEach
    void setup() {
        user = new User("Alice", "password123", "MEMBER", 2);
    }

    @Test
    void testUserCreation() {
        assertEquals("Alice", user.getUsername(), "Username should be correct.");
        assertTrue(user.authenticate("password123"), "Password authentication should work.");
        assertEquals("MEMBER", user.getRole(), "User role should be correct.");
    }

    @Test
    void testChangePassword() {
        user.changePassword("newpass");
        assertTrue(user.authenticate("newpass"), "User should authenticate with the new password.");
        assertFalse(user.authenticate("password123"), "Old password should no longer work.");
    }

    @Test
    void testBorrowBook() {
        Book book = new Book("Java Programming", "John Doe", "1234", "New");
        assertTrue(user.borrowBook(book), "User should be able to borrow a book.");
        assertFalse(book.isAvailable(), "Book should not be available after being borrowed.");
    }

    @Test
    void testReturnBook() {
        Book book = new Book("Java Programming", "John Doe", "1234", "New");
        user.borrowBook(book);
        assertTrue(user.returnBook(book), "User should be able to return the book.");
        assertTrue(book.isAvailable(), "Book should be available after return.");
    }

    @Test
    void testMaxBorrowLimit() {
        Book book1 = new Book("Java", "John", "1234", "New");
        Book book2 = new Book("Python", "Jane", "5678", "New");
        Book book3 = new Book("C++", "Smith", "9101", "New");

        assertTrue(user.borrowBook(book1));
        assertTrue(user.borrowBook(book2));
        assertFalse(user.borrowBook(book3), "User should not be able to borrow more than max limit.");
    }
}
