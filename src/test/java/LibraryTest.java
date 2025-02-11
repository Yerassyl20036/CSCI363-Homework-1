import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LibraryTest {
    private Library library;

    @BeforeEach
    void setup() {
        library = new Library();

        // Registering an admin and a member
        library.registerUser("AdminUser", "adminpass", "ADMIN", 5);
        library.registerUser("Alice", "password123", "MEMBER", 2);

        // Logging in as admin to add a book
        library.loginUser("AdminUser", "adminpass");
        library.addBook("Java Programming", "John Doe", "1234567890", "New");
        library.logoutUser();
    }

    @Test
    void testAdminCanAddBook() {
        library.loginUser("AdminUser", "adminpass");
        assertTrue(library.addBook("Data Structures", "Jane Smith", "5678", "Used"), "Admin should be able to add books.");
        library.logoutUser();
    }

    @Test
    void testMemberCannotAddBook() {
        library.loginUser("Alice", "password123");
        assertFalse(library.addBook("Algorithms", "Robert", "6789", "New"), "Member should NOT be able to add books.");
        library.logoutUser();
    }

    @Test
    void testFindBook() {
        assertNotNull(library.findBook("1234567890"), "Book should be found.");
    }

    @Test
    void testBorrowBookSuccess() {
        library.loginUser("Alice", "password123");
        assertTrue(library.borrowBook("1234567890"), "Member should be able to borrow an available book.");
        library.logoutUser();
    }

    @Test
    void testReturnBookSuccess() {
        library.loginUser("Alice", "password123");
        library.borrowBook("1234567890");
        assertTrue(library.returnBook("1234567890"), "Returning the book should succeed.");
        library.logoutUser();
    }

    @Test
    void testMemberCannotEditBook() {
        library.loginUser("Alice", "password123");
        assertFalse(library.editBook("1234567890", "Advanced Java", "Updated Author", "Used"), "Member should not be able to edit books.");
        library.logoutUser();
    }

    @Test
    void testAdminCanEditBook() {
        library.loginUser("AdminUser", "adminpass");
        assertTrue(library.editBook("1234567890", "Advanced Java", "Updated Author", "Used"), "Admin should be able to edit books.");
        library.logoutUser();
    }

    @Test
    void testLoginSuccess() {
        assertTrue(library.loginUser("Alice", "password123"), "Login should succeed with correct credentials.");
        library.logoutUser();
    }

    @Test
    void testLoginFailure() {
        assertFalse(library.loginUser("Alice", "wrongpass"), "Login should fail with incorrect password.");
    }
}
