
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.Mock;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

public class MockitoIntegrationTestSultan {

    @Mock
    private Book mockBook;

    @Mock
    private User mockUser;

    @Mock
    private Library mockLibrary;

    @Mock
    private Transaction mockTransaction;

    private List<Book> bookList;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        bookList = new ArrayList<>();
    }

    @Test
    void testUserBookIntegration() {
        // Setup mock Book behaviors
        when(mockBook.getTitle()).thenReturn("Clean Code");
        when(mockBook.getAuthor()).thenReturn("Robert Martin");
        when(mockBook.getISBN()).thenReturn("978-0132350884");
        when(mockBook.isAvailable()).thenReturn(true, false);
        doNothing().when(mockBook).borrow(anyString());

        // Create a real User with the mock Book
        User realUser = new User("alice", "password123", "MEMBER", 3);

        // Test the integration
        assertTrue(realUser.borrowBook(mockBook), "User should be able to borrow an available book");

        // Verify interactions
        verify(mockBook).borrow("alice");
        verify(mockBook).isAvailable();

        // Verify the book was added to user's borrowed books
        assertTrue(realUser.getBorrowedBooks().contains(mockBook), "Book should be in user's borrowed books");
    }

    @Test
    void testLibraryBookIntegration() {
        // Setup mock behaviors
        when(mockBook.getISBN()).thenReturn("978-0132350884");
        when(mockBook.isAvailable()).thenReturn(true);
        when(mockBook.getTitle()).thenReturn("Design Patterns");

        // Create a real Library with mocked components
        Library realLibrary = new Library();
        realLibrary.registerUser("admin", "admin123", "ADMIN", 5);
        realLibrary.loginUser("admin", "admin123");

        // Add the mock book to the library using reflection to access private field
        try {
            java.lang.reflect.Field booksField = Library.class.getDeclaredField("books");
            booksField.setAccessible(true);
            List<Book> books = (List<Book>) booksField.get(realLibrary);
            books.add(mockBook);
        } catch (Exception e) {
            fail("Failed to add mock book to library: " + e.getMessage());
        }

        // Test finding the book
        Book foundBook = realLibrary.findBook("978-0132350884");
        assertSame(mockBook, foundBook, "Library should find the correct book by ISBN");

        // Verify interactions
        verify(mockBook, atLeastOnce()).getISBN();
    }

    @Test
    void testLibraryUserIntegration() {
        // Setup a real Library
        Library realLibrary = new Library();

        // Setup mock User behaviors
        when(mockUser.getUsername()).thenReturn("bob");
        when(mockUser.getRole()).thenReturn("MEMBER");
        when(mockUser.authenticate("pass456")).thenReturn(true);
        when(mockUser.getMaxBorrowLimit()).thenReturn(2);

        // Add the mock user to the library using reflection
        try {
            java.lang.reflect.Field usersField = Library.class.getDeclaredField("users");
            usersField.setAccessible(true);
            List<User> users = (List<User>) usersField.get(realLibrary);
            users.add(mockUser);
        } catch (Exception e) {
            fail("Failed to add mock user to library: " + e.getMessage());
        }

        // Test login functionality
        realLibrary.loginUser("bob", "pass456");

        // Verify the user is logged in
        assertSame(mockUser, realLibrary.getLoggedInUser(), "Library should have the correct logged in user");

        // Verify interactions
        verify(mockUser).getUsername();
        verify(mockUser).authenticate("pass456");
    }

    @Test
    void testBookTransactionIntegration() {
        // Setup mock Book behaviors
        when(mockBook.getTitle()).thenReturn("Effective Java");
        when(mockBook.isAvailable()).thenReturn(true, false);
        doNothing().when(mockBook).setAvailable(anyBoolean());

        // Setup mock User behaviors
        when(mockUser.getUsername()).thenReturn("charlie");

        // Create a real Transaction with mocks
        Transaction realTransaction = new Transaction(mockUser, mockBook);

        // Test transaction details
        String details = realTransaction.getDetails();
        assertTrue(details.contains("charlie"), "Transaction details should include username");
        assertTrue(details.contains("Effective Java"), "Transaction details should include book title");

        // Test marking as returned
        assertFalse(realTransaction.isReturned(), "New transaction should not be marked as returned");
        realTransaction.markReturned();
        assertTrue(realTransaction.isReturned(), "Transaction should be marked as returned");

        // Verify interactions
        verify(mockUser, atLeastOnce()).getUsername();
        verify(mockBook, atLeastOnce()).getTitle();
    }

    @Test
    void testLibraryTransactionIntegration() {
        // Setup a real Library
        Library realLibrary = new Library();

        // Setup mock Book behaviors
        when(mockBook.getISBN()).thenReturn("978-0134685991");
        when(mockBook.isAvailable()).thenReturn(true);
        when(mockBook.getTitle()).thenReturn("Refactoring");
        doNothing().when(mockBook).setAvailable(anyBoolean());

        // Setup mock User behaviors
        when(mockUser.getUsername()).thenReturn("david");
        when(mockUser.getRole()).thenReturn("MEMBER");
        when(mockUser.borrowBook(any(Book.class))).thenReturn(true);

        // Add mocks to the library using reflection
        try {
            // Add mock book
            java.lang.reflect.Field booksField = Library.class.getDeclaredField("books");
            booksField.setAccessible(true);
            List<Book> books = (List<Book>) booksField.get(realLibrary);
            books.add(mockBook);

            // Add mock user and set as logged in
            java.lang.reflect.Field usersField = Library.class.getDeclaredField("users");
            usersField.setAccessible(true);
            List<User> users = (List<User>) usersField.get(realLibrary);
            users.add(mockUser);

            java.lang.reflect.Field loggedInUserField = Library.class.getDeclaredField("loggedInUser");
            loggedInUserField.setAccessible(true);
            loggedInUserField.set(realLibrary, mockUser);
        } catch (Exception e) {
            fail("Failed to setup library: " + e.getMessage());
        }

        // Test borrowing a book
        assertTrue(realLibrary.borrowBook("978-0134685991"), "Library should allow borrowing the book");

        // Verify a transaction was created
        try {
            java.lang.reflect.Field transactionsField = Library.class.getDeclaredField("transactions");
            transactionsField.setAccessible(true);
            List<Transaction> transactions = (List<Transaction>) transactionsField.get(realLibrary);
            assertEquals(1, transactions.size(), "A transaction should be created");
            assertEquals(mockUser, transactions.get(0).getUser(), "Transaction should have the correct user");
            assertEquals(mockBook, transactions.get(0).getBook(), "Transaction should have the correct book");
        } catch (Exception e) {
            fail("Failed to verify transactions: " + e.getMessage());
        }

        // Verify interactions
        verify(mockBook).setAvailable(false);
        verify(mockUser).borrowBook(mockBook);
    }
}
