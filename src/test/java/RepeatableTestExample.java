import org.junit.jupiter.api.RepeatedTest;
import static org.junit.jupiter.api.Assertions.*;

public class RepeatableTestExample {

    @RepeatedTest(5)
    void testBorrowBook() {
        Library library = new Library();
        library.registerUser("Bob", "password456", "MEMBER", 2);
        library.loginUser("Bob", "password456");

        library.registerUser("AdminUser", "adminpass", "ADMIN", 5);
        library.loginUser("AdminUser", "adminpass");
        library.addBook("Repeated Test Book", "Author", "12345", "New"); // ✅ Fixed method call
        library.logoutUser();

        assertTrue(library.borrowBook("12345"), "Book should be borrowed successfully.");
        library.logoutUser();
    }
}
