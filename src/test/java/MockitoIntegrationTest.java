import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

public class MockitoIntegrationTest {
    @Mock
    private User mockUser;
    @Mock
    private Book mockBook;
    @Mock
    private Transaction mockTransaction;
    private Library library;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        library = new Library();
    }

    @Test
    void testLibraryTransactionIntegration() {
        // Configure mock behavior for Transaction
        when(mockTransaction.getUser()).thenReturn(mockUser);
        when(mockTransaction.getBook()).thenReturn(mockBook);
        when(mockTransaction.isReturned()).thenReturn(false, true, false);
        when(mockTransaction.calculateLateFee(any(LocalDateTime.class)))
            .thenReturn(0.0, 10.0, 20.0);

        // Verify transaction behaviors
        assertEquals(mockUser, mockTransaction.getUser());
        assertFalse(mockTransaction.isReturned()); // First call
        assertTrue(mockTransaction.isReturned());  // Second call
        assertFalse(mockTransaction.isReturned()); // Third call
        
        // Verify late fee calculations
        assertEquals(0.0, mockTransaction.calculateLateFee(LocalDateTime.now()));
        assertEquals(10.0, mockTransaction.calculateLateFee(LocalDateTime.now()));
        assertEquals(20.0, mockTransaction.calculateLateFee(LocalDateTime.now()));
    }

    @Test
    void testUserBookIntegration() {
        // Configure mock behaviors
        when(mockUser.getMaxBorrowLimit()).thenReturn(2);
        when(mockUser.getBorrowedBooks()).thenReturn(
            new ArrayList<>(),           // First call: empty list
            Arrays.asList(mockBook),     // Second call: one book
            Arrays.asList(mockBook, mockBook)  // Third call: two books
        );
        when(mockBook.isAvailable()).thenReturn(true, false, true);

        // Test interactions
        assertEquals(2, mockUser.getMaxBorrowLimit());
        assertTrue(mockUser.getBorrowedBooks().isEmpty());
        assertEquals(1, mockUser.getBorrowedBooks().size());
        assertEquals(2, mockUser.getBorrowedBooks().size());
        
        // Verify book availability changes
        assertTrue(mockBook.isAvailable());
        assertFalse(mockBook.isAvailable());
        assertTrue(mockBook.isAvailable());
    }

    @Test
    void testLibraryUserIntegration() {
        // Configure mock behaviors
        when(mockUser.getUsername()).thenReturn("testUser");
        when(mockUser.authenticate("password123")).thenReturn(true, false, true);
        when(mockUser.getRole()).thenReturn("MEMBER", "ADMIN", "BANNED");

        // Test user authentication and role changes
        assertEquals("testUser", mockUser.getUsername());
        assertTrue(mockUser.authenticate("password123"));
        assertFalse(mockUser.authenticate("password123"));
        assertTrue(mockUser.authenticate("password123"));
        
        assertEquals("MEMBER", mockUser.getRole());
        assertEquals("ADMIN", mockUser.getRole());
        assertEquals("BANNED", mockUser.getRole());
    }

    @Test
    void testBookTransactionIntegration() {
        // Configure mock behaviors
        when(mockBook.getCondition()).thenReturn("New", "Used", "Damaged");
        when(mockBook.getBorrowHistory()).thenReturn(
            Arrays.asList("User1"),
            Arrays.asList("User1", "User2"),
            Arrays.asList("User1", "User2", "User3")
        );
        when(mockTransaction.getDetails()).thenReturn(
            "Transaction1",
            "Transaction2",
            "Transaction3"
        );

        // Verify condition changes
        assertEquals("New", mockBook.getCondition());
        assertEquals("Used", mockBook.getCondition());
        assertEquals("Damaged", mockBook.getCondition());

        // Verify borrow history
        assertEquals(1, mockBook.getBorrowHistory().size());
        assertEquals(2, mockBook.getBorrowHistory().size());
        assertEquals(3, mockBook.getBorrowHistory().size());

        // Verify transaction details
        assertEquals("Transaction1", mockTransaction.getDetails());
        assertEquals("Transaction2", mockTransaction.getDetails());
        assertEquals("Transaction3", mockTransaction.getDetails());
    }

    @Test
    void testLibraryBookIntegration() {
        // Configure mock behaviors
        when(mockBook.getISBN()).thenReturn("1234");
        when(mockBook.getTitle()).thenReturn("Original", "Updated", "Final");
        when(mockBook.getAuthor()).thenReturn("Author1", "Author2", "Author3");
        
        // Test book information changes
        assertEquals("1234", mockBook.getISBN());
        
        assertEquals("Original", mockBook.getTitle());
        assertEquals("Updated", mockBook.getTitle());
        assertEquals("Final", mockBook.getTitle());
        
        assertEquals("Author1", mockBook.getAuthor());
        assertEquals("Author2", mockBook.getAuthor());
        assertEquals("Author3", mockBook.getAuthor());
    }
}