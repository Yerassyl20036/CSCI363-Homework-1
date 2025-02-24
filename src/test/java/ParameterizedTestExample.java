import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.*;

public class ParameterizedTestExample {

    @ParameterizedTest
    @ValueSource(strings = {"1001", "1002", "1003"})
    void testFindBook(String isbn) {
        Library library = new Library();
        library.registerUser("AdminUser", "adminpass", "ADMIN", 5);
        library.loginUser("AdminUser", "adminpass");
        library.addBook("Parameterized Test Book", "Author", isbn, "Used");
        library.logoutUser();

        assertNotNull(library.findBook(isbn), "Book with ISBN " + isbn + " should be found.");
    }

    @ParameterizedTest
    @ValueSource(strings = {"wrongpass", "12345", "adminpass"})
    void testLoginWithMultiplePasswords(String password) {
        Library library = new Library();
        library.registerUser("AdminUser", "adminpass", "ADMIN", 5);

        if (password.equals("adminpass")) {
            assertDoesNotThrow(() -> library.loginUser("Alice", "password123"));
        } else {
            assertThrows(IllegalArgumentException.class, () -> library.loginUser("Alice", "wrongpass"));
        }
    }
}
