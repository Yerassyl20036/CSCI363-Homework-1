import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class MockitoIntegrationTest {
    @Mock
    private User mockUser;
    @Mock
    private Book mockBook;
    private Library library;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        library = new Library();
    }

    @Test
    void testLibraryUserAuthenticationIntegration() {
        // Configure mock behavior
        when(mockUser.getUsername()).thenReturn("testUser");
        when(mockUser.authenticate("correctPass")).thenReturn(true);
        when(mockUser.authenticate("wrongPass")).thenReturn(false);
        when(mockUser.getRole()).thenReturn("MEMBER");

        // Verify authentication behavior
        assertTrue(mockUser.authenticate("correctPass"));
        assertFalse(mockUser.authenticate("wrongPass"));
        assertEquals("MEMBER", mockUser.getRole());
    }

    @Test
    void testLibraryBookAvailabilityIntegration() {
        // Configure mock behavior
        when(mockBook.getISBN()).thenReturn("12345");
        when(mockBook.isAvailable()).thenReturn(true, false, true);
        when(mockBook.getTitle()).thenReturn("Mock Book");

        // Test book availability changes
        assertTrue(mockBook.isAvailable());
        assertEquals("Mock Book", mockBook.getTitle());
        
        // Verify the second call returns false
        assertFalse(mockBook.isAvailable());
        
        // Verify the third call returns true
        assertTrue(mockBook.isAvailable());
    }

    @Test
    void testUserBookBorrowingIntegration() {
        // Configure mock behavior
        when(mockBook.isAvailable()).thenReturn(true);
        when(mockUser.borrowBook(mockBook)).thenReturn(true);
        when(mockUser.getRole()).thenReturn("MEMBER");
        
        // Test borrowing interaction
        assertTrue(mockUser.borrowBook(mockBook));
        verify(mockBook, times(1)).isAvailable();
    }

    @Test
    void testLibraryBookManagementIntegration() {
        // Configure mock behavior for book management
        when(mockBook.getISBN()).thenReturn("12345");
        when(mockBook.getTitle()).thenReturn("Original Title", "New Title");
        when(mockBook.getCondition()).thenReturn("New", "Used", "Damaged");

        // Test multiple conditions
        assertEquals("Original Title", mockBook.getTitle());
        assertEquals("New", mockBook.getCondition());
        assertEquals("Used", mockBook.getCondition());
        assertEquals("Damaged", mockBook.getCondition());
    }

    @Test
    void testUserBookReturnIntegration() {
        // Configure mock behavior
        when(mockBook.isAvailable()).thenReturn(false, true);
        when(mockUser.returnBook(mockBook)).thenReturn(true);
        doNothing().when(mockBook).returnBook();

        // Test return interaction
        assertTrue(mockUser.returnBook(mockBook));
        verify(mockBook).returnBook();
        assertTrue(mockBook.isAvailable());
    }
}