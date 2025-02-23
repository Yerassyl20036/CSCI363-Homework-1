import static org.junit.jupiter.api.Assertions.assertTrue;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.RepeatedTest;

public class RepeatableTestExample {
    private static Library library;

    @BeforeAll
    static void setup(){
        library = new Library();
        library.registerUser("Alice", "password123", "MEMBER", 2);
    }

    @RepeatedTest(5)
    void testBorrowBookRepeatedly() {
        library.loginUser("Alice", "password123");

        library.registerUser("AdminUser", "adminpass", "ADMIN", 5);
        library.loginUser("AdminUser", "adminpass");
        library.addBook("Repeated Test Book", "Author", "1234", "New");
        library.logoutUser();

        library.loginUser("Alice", "password123");
        boolean borrowed = library.borrowBook("1234");
        assertTrue(borrowed);
        
        // Fix: Provide a due date (assume due date was 10 days ago)
        LocalDateTime dueDate = LocalDateTime.now().minusDays(10);
        library.returnBook("1234", dueDate, false);
        library.logoutUser();
    }
}
