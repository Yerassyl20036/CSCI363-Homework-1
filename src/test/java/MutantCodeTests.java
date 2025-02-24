import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class MutantCodeTests {
    private Library library = new Library();

    @Test
    void mutant_ChangeExpectedMessage() {
        library.loginUser("Alice", "password123");
        library.borrowBook("1234567890");
        LocalDateTime dueDate = LocalDateTime.now().minusDays(10);
        assertEquals("Book successfully returned.", library.returnBook("1234567890", dueDate, false)); // Mutated expected message
        library.logoutUser();
    }

    @Test
    void mutant_ModifyDueDate() {
        library.loginUser("Alice", "password123");
        library.borrowBook("1234567890");
        LocalDateTime dueDate = LocalDateTime.now().minusDays(5); // Mutated due date
        assertEquals("Book returned successfully.", library.returnBook("1234567890", dueDate, false));
        library.logoutUser();
    }

    @Test
    void mutant_FlipBooleanIsDamaged() {
        library.loginUser("Alice", "password123");
        library.borrowBook("1234567890");
        LocalDateTime dueDate = LocalDateTime.now().minusDays(10);
        assertEquals("Book returned successfully.", library.returnBook("1234567890", dueDate, true)); // Mutated: Changed false to true
        library.logoutUser();
    }

    @Test
    void mutant_RemoveBorrowStep() {
        library.loginUser("Alice", "password123");
        // library.borrowBook("1234567890"); // Mutated: Removed borrowing step
        LocalDateTime dueDate = LocalDateTime.now().minusDays(10);
        assertEquals("Book returned successfully.", library.returnBook("1234567890", dueDate, false));
        library.logoutUser();
    }

    // Mutants for testFindBook
    @ParameterizedTest
    @ValueSource(strings = {"1001", "1002", "1003"})
    void mutant_RemoveAdminLogin(String isbn) {
        Library library = new Library();
        library.registerUser("AdminUser", "adminpass", "ADMIN", 5);
        // library.loginUser("AdminUser", "adminpass"); // Mutated: Removed admin login step
        library.addBook("Parameterized Test Book", "Author", isbn, "Used");
        library.logoutUser();
        assertNotNull(library.findBook(isbn), "Book with ISBN " + isbn + " should be found.");
    }

    @ParameterizedTest
    @ValueSource(strings = {"2001", "2002", "2003"}) // Mutated: Changed test ISBN values
    void mutant_ModifyISBNInput(String isbn) {
        Library library = new Library();
        library.registerUser("AdminUser", "adminpass", "ADMIN", 5);
        library.loginUser("AdminUser", "adminpass");
        library.addBook("Parameterized Test Book", "Author", isbn, "Used");
        library.logoutUser();
        assertNotNull(library.findBook(isbn), "Book with ISBN " + isbn + " should be found.");
    }

    @ParameterizedTest
    @ValueSource(strings = {"1001", "1002", "1003"})
    void mutant_ChangeAssertionToNull(String isbn) {
        Library library = new Library();
        library.registerUser("AdminUser", "adminpass", "ADMIN", 5);
        library.loginUser("AdminUser", "adminpass");
        library.addBook("Parameterized Test Book", "Author", isbn, "Used");
        library.logoutUser();
        assertNull(library.findBook(isbn), "Book with ISBN " + isbn + " should NOT be found."); // Mutated: Changed assertion
    }

    @ParameterizedTest
    @ValueSource(strings = {"1001", "1002", "1003"})
    void mutant_RemoveLogoutStep(String isbn) {
        Library library = new Library();
        library.registerUser("AdminUser", "adminpass", "ADMIN", 5);
        library.loginUser("AdminUser", "adminpass");
        library.addBook("Parameterized Test Book", "Author", isbn, "Used");
        // library.logoutUser(); // Mutated: Removed logout step
        assertNotNull(library.findBook(isbn), "Book with ISBN " + isbn + " should be found.");
    }
}
