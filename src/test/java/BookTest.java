import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BookTest {
    private Book book;

    @BeforeEach
    void setup() {
        book = new Book("Java Programming", "John Doe", "1234567890", "New");
    }

    @Test
    void testBookCreation() {
        assertEquals("Java Programming", book.getTitle(), "Book title should be correct.");
        assertEquals("John Doe", book.getAuthor(), "Book author should be correct.");
        assertEquals("1234567890", book.getISBN(), "Book ISBN should be correct.");
        assertEquals("New", book.getCondition(), "Book condition should be correct.");
        assertTrue(book.isAvailable(), "Book should be available when created.");
    }

    @Test
    void testBorrowBook() {
        book.borrow("Alice");
        assertFalse(book.isAvailable(), "Book should not be available after being borrowed.");
        assertTrue(book.getBorrowHistory().contains("Alice"), "Borrow history should include the borrower.");
    }

    @Test
    void testReturnBook() {
        book.borrow("Alice");
        book.returnBook();
        assertTrue(book.isAvailable(), "Book should be available after being returned.");
    }

    @Test
    void testSetBookDetails() {
        book.setTitle("Advanced Java");
        book.setAuthor("Jane Doe");
        book.setCondition("Used");

        assertEquals("Advanced Java", book.getTitle(), "Book title should be updated.");
        assertEquals("Jane Doe", book.getAuthor(), "Book author should be updated.");
        assertEquals("Used", book.getCondition(), "Book condition should be updated.");
    }
}
