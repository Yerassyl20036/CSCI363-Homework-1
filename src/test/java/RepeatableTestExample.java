import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.RepeatedTest;

public class RepeatableTestExample {
    private static  Library library;

    @BeforeAll
    static void setup(){
        library = new Library();
        library.registerUser("Alice", "password123", "MEMBER", 2);

    }

    @RepeatedTest(5)
    void testBorrowBookRepeatedly() {
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
