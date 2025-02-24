import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import exceptions.BookNotFoundException;

import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

public class MutantCodeTests {
    private Library library;

    @BeforeEach
    void setup() {  
        library = new Library();
        
        // Admin logs in and adds a book
        library.registerUser("admin", "adminpass", "ADMIN", 10);
        library.loginUser("admin", "adminpass");
        library.addBook("Test Book", "Test Author", "1234567890", "Good");
        library.logoutUser();

        library.registerUser("Alice", "password123", "MEMBER", 2);
    }    

    @Test
    void testReturnBookSuccess_Mutant1() { 
        library.loginUser("Alice", "password123");
        library.borrowBook("1234567890");

        LocalDateTime dueDate = LocalDateTime.now().minusDays(10);
        assertEquals("Book returned successfully. Late fee: $10.0.", library.returnBook("1234567890", dueDate, false));

        library.logoutUser();
    }

    @Test
    void testReturnBookSuccess_Mutant2() { 
        library.loginUser("Alice", "password123");
        library.borrowBook("1234567890");

        LocalDateTime dueDate = LocalDateTime.now().minusDays(10);
        assertThrows(BookNotFoundException.class, () -> { 
            library.returnBook("0000", dueDate, false);
        });

        library.logoutUser();
    }

    @Test
    void testFindNonExistentBookThrowsException_Mutant3() {
        assertThrows(BookNotFoundException.class, () -> { 
            library.findBook("9999");
        });
    }

    @Test
    void testReturnBookSuccess_Mutant4() { 
        library.loginUser("Alice", "password123");
        library.borrowBook("1234567890");

        LocalDateTime dueDate = LocalDateTime.now().minusDays(10);
        library.returnBook("1234567890", dueDate, false);

        library.logoutUser();
    }
}
