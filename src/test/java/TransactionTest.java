import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TransactionTest {
    private Transaction transaction;
    private User user;
    private Book book;

    @BeforeEach
    void setup() {
        user = new User("Alice", "password123", "MEMBER", 2);
        book = new Book("Java Programming", "John Doe", "1234", "New");
        transaction = new Transaction(user, book);
    }

    @Test
    void testTransactionCreation() {
        assertEquals(user, transaction.getUser(), "Transaction should be associated with the correct user.");
        assertEquals(book, transaction.getBook(), "Transaction should be associated with the correct book.");
        assertFalse(transaction.isReturned(), "Transaction should initially be unreturned.");
    }

    @Test
    void testMarkReturned() {
        transaction.markReturned();
        assertTrue(transaction.isReturned(), "Transaction should be marked as returned.");
    }

    @Test
    void testTransactionDetails() {
        transaction.markReturned();
        String details = transaction.getDetails();
        assertTrue(details.contains("Alice"), "Transaction should include the username.");
        assertTrue(details.contains("Java Programming"), "Transaction should include the book title.");
    }
}
