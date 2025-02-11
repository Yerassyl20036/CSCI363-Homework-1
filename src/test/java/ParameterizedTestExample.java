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
        library.addBook("Test Book", "Author", isbn, "Used"); // ✅ Fixed method call
        library.logoutUser();

        assertNotNull(library.findBook(isbn), "Book with ISBN " + isbn + " should be found.");
    }
}
