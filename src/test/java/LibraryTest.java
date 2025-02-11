import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import exceptions.BookNotFoundException;
import exceptions.BookAlreadyBorrowedException;

public class LibraryTest {
    private Library library;

    @BeforeEach
    void setup() {
        library = new Library();
        library.registerUser("AdminUser", "adminpass", "ADMIN", 5);
        library.registerUser("Alice", "password123", "MEMBER", 2);
        library.loginUser("AdminUser", "adminpass");
        library.addBook("Java Programming", "John Doe", "1234567890", "New");
        library.logoutUser();
    }

    @Test
    void testFindNonExistentBookThrowsException() {
        assertThrows(BookNotFoundException.class, () -> {
            library.findBook("9999");
        }, "Searching for a non-existent book should throw BookNotFoundException.");
    }

    @Test
    void testBorrowAlreadyBorrowedBookThrowsException() {
        library.loginUser("Alice", "password123");
        library.borrowBook("1234567890"); 

        assertThrows(BookAlreadyBorrowedException.class, () -> {
            library.borrowBook("1234567890"); 
        }, "Borrowing an already borrowed book should throw BookAlreadyBorrowedException.");
    }

    @Test
    void testAdminCanAddBook() {
        library.loginUser("AdminUser", "adminpass");
        assertTrue(library.addBook("Data Structures", "Jane Smith", "5678", "Used"), "Admin should be able to add books.");
        library.logoutUser();
    }

    @Test
    void testMemberCannotAddBook() {
        library.loginUser("Alice", "password123");
        assertFalse(library.addBook("Algorithms", "Robert", "6789", "New"), "Member should NOT be able to add books.");
        library.logoutUser();
    }

    @Test
    void testFindBook() {
        assertNotNull(library.findBook("1234567890"), "Book should be found.");
    }

    @Test
    void testBorrowBookSuccess() {
        library.loginUser("Alice", "password123");
        assertTrue(library.borrowBook("1234567890"), "Member should be able to borrow an available book.");
        library.logoutUser();
    }

    @Test
    void testReturnBookSuccess() {
        library.loginUser("Alice", "password123");
        library.borrowBook("1234567890");
        assertTrue(library.returnBook("1234567890"), "Returning the book should succeed.");
        library.logoutUser();
    }

    @Test
    void testMemberCannotEditBook() {
        library.loginUser("Alice", "password123");
        assertFalse(library.editBook("1234567890", "Advanced Java", "Updated Author", "Used"), "Member should not be able to edit books.");
        library.logoutUser();
    }

    @Test
    void testAdminCanEditBook() {
        library.loginUser("AdminUser", "adminpass");
        assertTrue(library.editBook("1234567890", "Advanced Java", "Updated Author", "Used"), "Admin should be able to edit books.");
        library.logoutUser();
    }

    @Test
    void testLoginSuccess() {
        assertTrue(library.loginUser("Alice", "password123"), "Login should succeed with correct credentials.");
        library.logoutUser();
    }

    @Test
    void testLoginFailure() {
        assertFalse(library.loginUser("Alice", "wrongpass"), "Login should fail with incorrect password.");
    }


 @Test
    void testAddMultipleBooksAndVerify() {
        library.loginUser("AdminUser", "adminpass");

        // Add multiple books
        library.addBook("Clean Code", "Robert C. Martin", "1111", "New");
        library.addBook("Design Patterns", "Erich Gamma", "2222", "Used");
        library.addBook("Refactoring", "Martin Fowler", "3333", "New");

        // Retrieve books from the library
        List<Book> bookList = library.getBooks();
        String[] expectedTitles = {"Java Programming", "Clean Code", "Design Patterns", "Refactoring"};
        String[] actualTitles = bookList.stream().map(Book::getTitle).toArray(String[]::new);

        // Verify all books are added correctly
        assertArrayEquals(expectedTitles, actualTitles, "Books in the library should match expected list.");

        library.logoutUser();
    }
}
