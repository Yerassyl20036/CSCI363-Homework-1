import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import exceptions.BookAlreadyBorrowedException;
import exceptions.BookNotFoundException;
import java.lang.IllegalArgumentException;

public class LibraryTest {
    private Library library;

    @BeforeEach
    void setup() {
        library = new Library();
        library.registerUser("AdminUser", "adminpass", "ADMIN", 5);
        library.registerUser("Alice", "password123", "MEMBER", 2);
        library.loginUser("AdminUser", "adminpass");
        library.addBook("Java Programming", "John Doe", "1234567890", "New");
        library.logoutUser();
    }

    @Test
    void testReturnBookSuccess() {
        library.loginUser("Alice", "password123");
        library.borrowBook("1234567890");

        // Fix: Provide a due date (assume due date was 10 days ago)
        LocalDateTime dueDate = LocalDateTime.now().minusDays(10);
        assertEquals("Book returned successfully. Late fee: $10.0.", library.returnBook("1234567890", dueDate, false));

        library.logoutUser();
    }

    @Test
    void testFindNonExistentBookThrowsException() {
        assertThrows(BookNotFoundException.class, () -> {
            library.findBook("9999");
        });
    }

    @Test
    void testBorrowAlreadyBorrowedBookThrowsException() {
        library.loginUser("Alice", "password123");
        library.borrowBook("1234567890"); 

        assertThrows(BookAlreadyBorrowedException.class, () -> {
            library.borrowBook("1234567890"); 
        });
    }

    @Test
    void testAdminCanAddBook() {
        library.loginUser("AdminUser", "adminpass");
        assertTrue(library.addBook("Data Structures", "Jane Smith", "5678", "Used"));
        library.logoutUser();
    }

    @Test
    void testMemberCannotAddBook() {
        library.loginUser("Alice", "password123");
        assertFalse(library.addBook("Algorithms", "Robert", "6789", "New"));
        library.logoutUser();
    }

    @Test
    void testFindBook() {
        assertNotNull(library.findBook("1234567890"));
    }

    @Test
    void testBorrowBookSuccess() {
        library.loginUser("Alice", "password123");
        assertTrue(library.borrowBook("1234567890"));
        library.logoutUser();
    }

    @Test
    void testLoginSuccess() {
        assertDoesNotThrow(() -> library.loginUser("Alice", "password123"));
        library.logoutUser();
    }

    @Test
    void testLoginFailure() {
        assertThrows(IllegalArgumentException.class, () -> library.loginUser("Alice", "wrongpass"));
    }
}
