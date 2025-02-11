import org.junit.jupiter.api.RepeatedTest;
import static org.junit.jupiter.api.Assertions.*;

public class RepeatableTestExample {
    private Library library;

    @RepeatedTest(5)
    void testBorrowBookRepeatedly() {
        library = new Library();
        library.registerUser("Alice", "password123", "MEMBER", 2);
        library.loginUser("Alice", "password123");

        // Adding a book as an admin
        library.registerUser("AdminUser", "adminpass", "ADMIN", 5);
        library.loginUser("AdminUser", "adminpass");
        library.addBook("Repeated Test Book", "Author", "1234", "New");
        library.logoutUser();

        // Attempt to borrow the book
        library.loginUser("Alice", "password123");
        boolean borrowed = library.borrowBook("1234");
        assertTrue(borrowed, "User should be able to borrow the book multiple times after returning.");
        
        // Return the book to allow repeated borrowing
        library.returnBook("1234");
        library.logoutUser();
    }
}
