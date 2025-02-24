import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class WhiteBoxTests {
    private Library library;

    @BeforeEach
    void setup() {
        library = new Library();
        library.registerUser("AdminUser", "adminpass", "ADMIN", 10);
        library.loginUser("AdminUser", "adminpass"); // ✅ Login as ADMIN
        library.addBook("Java Programming", "John Doe", "1234", "New"); // ✅ Successfully add book
        library.logoutUser();

        library.registerUser("Alice", "password123", "MEMBER", 2);
        library.loginUser("Alice", "password123"); // ✅ Switch to MEMBER
        library.borrowBook("1234"); 
    }

    @Test
    void testReturnBookNoFeesNoBan() { // P1
        LocalDateTime dueDate = LocalDateTime.now();
        String result = library.returnBook("1234", dueDate, false);
        assertEquals("Book returned successfully.", result);
    }

    @Test
    void testReturnBookNoFeesBanned() { // P2
        library.getLoggedInUser().addFine(60); // ✅ Fixed
        LocalDateTime dueDate = LocalDateTime.now();
        String result = library.returnBook("1234", dueDate, false);
        assertTrue(result.contains("banned"));
    }

    @Test
    void testReturnBookDamageFee() { // P3
        LocalDateTime dueDate = LocalDateTime.now();
        String result = library.returnBook("1234", dueDate, true);
        assertTrue(result.contains("Damage fee"));
    }

    @Test
    void testReturnBookDamageFeeAndBan() { // P4
        library.getLoggedInUser().addFine(60); // ✅ Fixed
        LocalDateTime dueDate = LocalDateTime.now();
        String result = library.returnBook("1234", dueDate, true);
        assertTrue(result.contains("banned") && result.contains("Damage fee"));
    }

    @Test
    void testReturnBookLateFee() { // P5
        LocalDateTime dueDate = LocalDateTime.now().minusDays(5);
        String result = library.returnBook("1234", dueDate, false);
        assertTrue(result.contains("Late fee"));
    }

    @Test
    void testReturnBookLateFeeAndBan() { // P6
        library.getLoggedInUser().addFine(60); // ✅ Fixed
        LocalDateTime dueDate = LocalDateTime.now().minusDays(5);
        String result = library.returnBook("1234", dueDate, false);
        assertTrue(result.contains("banned") && result.contains("Late fee"));
    }

    @Test
    void testReturnBookLateAndDamageFee() { // P7
        LocalDateTime dueDate = LocalDateTime.now().minusDays(5);
        String result = library.returnBook("1234", dueDate, true);
        assertTrue(result.contains("Late fee") && result.contains("Damage fee"));
    }

    @Test
    void testReturnBookLateDamageFeeAndBan() { // P8
        library.getLoggedInUser().addFine(60); // ✅ Fixed
        LocalDateTime dueDate = LocalDateTime.now().minusDays(5);
        String result = library.returnBook("1234", dueDate, true);
        assertTrue(result.contains("banned") && result.contains("Late fee") && result.contains("Damage fee"));
    }

    @Test
    void testReturnBookNoTransaction() { // P9
        library.registerUser("Bob", "password456", "MEMBER", 2);
        library.loginUser("Bob", "password456");
        LocalDateTime dueDate = LocalDateTime.now();
        String result = library.returnBook("1234", dueDate, false);
        assertEquals("Error: No matching transaction found for this book.", result);
    }

    @Test
    void testReturnBookNotFound() { // P10
        LocalDateTime dueDate = LocalDateTime.now();
        String result = library.returnBook("9999", dueDate, false);
        assertEquals("Error: Book not found.", result);
    }

    @Test
    void testReturnBookNoUserLoggedIn() { // P11
        library.logoutUser();
        LocalDateTime dueDate = LocalDateTime.now();
        String result = library.returnBook("1234", dueDate, false);
        assertEquals("Error: No user logged in.", result);
    }
}
