
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import exceptions.BookAlreadyBorrowedException;

public class MutatedLibraryTest {

    private MutatedLibrary library;

    @BeforeEach
    void setup() {
        library = new MutatedLibrary();
        library.registerUser("AdminUser", "adminpass", "ADMIN", 5);
        library.registerUser("Alice", "password123", "MEMBER", 2);
        library.loginUser("AdminUser", "adminpass");
        library.addBook("Java Programming", "John Doe", "1234567890", "New");
        library.logoutUser();
    }

    @Test
    void testBorrowMutation1() {
        library.loginUser("Alice", "password123");
        assertThrows(BookAlreadyBorrowedException.class, () -> {
            library.borrowBook_mutated1("1234567890");
        }, "Mutation 1: Negating availability check should cause an exception when borrowing an available book.");
        library.logoutUser();
    }

    @Test
    void testBorrowMutation2() {
        library.loginUser("Alice", "password123");
        assertTrue(library.borrowBook_mutated2("1234567890"), "Mutation 2: Book should be borrowed.");
        assertTrue(library.findBook("1234567890").isAvailable(), "Mutation 2: Book should still be available after borrowing.");
        library.logoutUser();
    }

    @Test
    void testBorrowMutation3() {
        library.loginUser("Alice", "password123");
        assertTrue(library.borrowBook_mutated3("1234567890"), "Mutation 3: Book should be borrowed.");
        assertTrue(library.findBook("1234567890").isAvailable(), "Mutation 3: Book should remain available after borrowing.");
        library.logoutUser();
    }

    @Test
    void testBorrowMutation4() {
        library.loginUser("Alice", "password123");
        assertTrue(library.borrowBook_mutated4("1234567890"), "Mutation 4: Book should be borrowed.");
        assertFalse(library.getTransactions().stream().anyMatch(t -> t.getBook().getISBN().equals("1234567890")), "Mutation 4: No transaction should be recorded.");
        library.logoutUser();
    }

    @Test
    void testReturnMutation1() {
        library.loginUser("Alice", "password123");
        library.borrowBook("1234567890");
        LocalDateTime dueDate = LocalDateTime.now();
        String result = library.returnBook_mutated1("1234567890", dueDate, false);

        assertTrue(result.contains("Late fee"), "Mutation 1: A late fee should be incorrectly applied even when not late.");
        library.logoutUser();
    }

    @Test
    void testReturnMutation2() {
        library.loginUser("Alice", "password123");
        library.borrowBook("1234567890");
        LocalDateTime dueDate = LocalDateTime.now();
        String result = library.returnBook_mutated2("1234567890", dueDate, false);

        assertTrue(result.contains("Damage fee"), "Mutation 2: A damage fee should be incorrectly applied even when not damaged.");
        library.logoutUser();
    }

    @Test
    void testReturnMutation3() {
        library.loginUser("Alice", "password123");
        library.borrowBook("1234567890");
        LocalDateTime dueDate = LocalDateTime.now().minusDays(10);
        library.returnBook_mutated3("1234567890", dueDate, false);

        assertFalse(library.findBook("1234567890").isAvailable(), "Mutation 3: Book should not be available after return.");
        library.logoutUser();
    }

    @Test
    void testReturnMutation4() {
        library.loginUser("Alice", "password123");
        library.borrowBook("1234567890");
        LocalDateTime dueDate = LocalDateTime.now().minusDays(10);
        String result = library.returnBook_mutated4("1234567890", dueDate, true);

        assertFalse(result.contains("Late fee") || result.contains("Damage fee"), "Mutation 4: No fines should be applied, even when due.");
        library.logoutUser();
    }

}
