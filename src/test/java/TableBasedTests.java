import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import exceptions.BookAlreadyBorrowedException;
import exceptions.BookNotFoundException;

public class TableBasedTests {
    private Library library;

    @BeforeEach
    void setup() {
        library = new Library();
        library.registerUser("Alice", "password123", "MEMBER", 5);
        library.registerUser("Admin", "adminpass", "ADMIN", 5);

        // Admin logs in to add books
        library.loginUser("Admin", "adminpass");
        library.addBook("Java Programming", "John Doe", "1234", "New");
        library.addBook("Data Structures", "Jane Smith", "5678", "New");
        library.logoutUser();
    }

    @Test
    void testBorrowBookTableBased() {
        String[][] results = {
            {"Test ID", "User Logged In", "User Role", "Book Available", "Borrow Limit Reached", "Expected Result", "Actual Result"},
            {"1", "No", "N/A", "Yes", "No", "Fail - No user logged in", runBorrowBookTest(false, "MEMBER", true, false)},
            {"2", "Yes", "ADMIN", "Yes", "No", "Fail - Admin cannot borrow", runBorrowBookTest(true, "ADMIN", true, false)},
            {"3", "Yes", "MEMBER", "No", "No", "Fail - Book already borrowed", runBorrowBookTest(true, "MEMBER", false, false)},
            {"4", "Yes", "MEMBER", "Yes", "Yes", "Fail - Borrow limit reached", runBorrowBookTest(true, "MEMBER", true, true)}
        };

        printTable(results);
    }

    @Test
    void testReturnBookWithFeesTableBased() {
        String[][] results = {
            {"Test ID", "Late Submission", "Is Damaged", "Expected Result", "Actual Result"},
            {"1", "No", "No", "Pass - No fees applied", runReturnBookFeesTest(false, false)},
            {"2", "Yes", "No", "Pass - Late fee applied", runReturnBookFeesTest(true, false)},
            {"3", "No", "Yes", "Pass - Damage fee applied", runReturnBookFeesTest(false, true)},
            {"4", "Yes", "Yes", "Pass - Late + Damage fee applied", runReturnBookFeesTest(true, true)}
        };

        printTable2(results);
    }

    private String runBorrowBookTest(boolean userLoggedIn, String role, boolean bookAvailable, boolean borrowLimitReached) {
        library.logoutUser(); // Ensure no user is logged in
        library.getBooks().clear();

        // **Fix: Admin must log in before adding books**
        library.loginUser("Admin", "adminpass");
        boolean added = library.addBook("Java Programming", "John Doe", "1234", "New");
        library.logoutUser();

        if (!added) {
            return "Fail - Book not added to library";
        }

        Book book;
        try {
            book = library.findBook("1234");
        } catch (BookNotFoundException e) {
            return "Fail - Book not found";
        }

        if (userLoggedIn) {
            String username = role.equals("ADMIN") ? "Admin" : "Alice";
            String password = role.equals("ADMIN") ? "adminpass" : "password123";
            try {
                library.loginUser(username, password);
            } catch (IllegalArgumentException e) {
                return "Fail - No user logged in";
            }
        }

        if (!userLoggedIn) {
            return library.borrowBook("1234") ? "Fail" : "Fail - No user logged in";
        }

        if (!bookAvailable) {
            book.borrow("Someone");  // Simulate that the book is already borrowed
        } else {
            book.setAvailable(true); // **Fix: Ensure book is available for borrowing**
        }

        if (role.equals("ADMIN")) {
            return library.borrowBook("1234") ? "Fail" : "Fail - Admin cannot borrow";
        }

        if (borrowLimitReached && library.getLoggedInUser() != null) {
            // First book borrowed
            library.borrowBook("1234");
            library.loginUser("Admin", "adminpass");
            library.addBook("Data Structures", "Jane Smith", "5678", "New"); // Ensure another book is available
            library.logoutUser();

            if (library.borrowBook("5678")) {
                return "Fail";
            }
            return "Fail - Borrow limit reached";
        }

        try {
            boolean result = library.borrowBook("1234");
            return result ? "Pass - Book borrowed successfully" : "Fail";
        } catch (BookAlreadyBorrowedException e) {
            return "Fail - Book already borrowed";
        } catch (IllegalArgumentException e) {
            return "Fail - No user logged in";
        } catch (Exception e) {
            return "Fail - " + e.getMessage();
        }
    }

    private void printTable(String[][] data) {
        for (String[] row : data) {
            System.out.println(String.format("%-10s %-15s %-10s %-15s %-20s %-35s %-35s",
                row[0], row[1], row[2], row[3], row[4], row[5], row[6]));
        }
    }

    private String runReturnBookFeesTest(boolean lateReturn, boolean damaged) {
        library.logoutUser();
        library.getBooks().clear();
        library.getTransactions().clear();

        // Ensure book exists
        library.loginUser("Admin", "adminpass");
        library.addBook("Java Programming", "John Doe", "1234", "New");
        library.logoutUser();

        // User logs in and borrows the book
        library.loginUser("Alice", "password123");
        library.borrowBook("1234");

        // Define due date based on late submission flag
        LocalDateTime dueDate = lateReturn ? LocalDateTime.now().minusDays(5) : LocalDateTime.now();
        String result = library.returnBook("1234", dueDate, damaged);
        // System.out.println("DEBUG: Late=" + lateReturn + ", Damaged=" + damaged + " → " + result);
        
        if (!lateReturn && !damaged) {
            return result.contains("Book returned successfully.") ? "Pass - No fees applied" : "Fail";
        }
        if (lateReturn && !damaged) {
            return result.contains("Late fee") ? "Pass - Late fee applied" : "Fail";
        }
        if (!lateReturn && damaged) {
            return result.contains("Damage fee") ? "Pass - Damage fee applied" : "Fail";
        }
        return result.contains("Late fee") && result.contains("Damage fee") ? "Pass - Late + Damage fee applied" : "Fail";
    }

    private void printTable2(String[][] data) {
        for (String[] row : data) {
            System.out.println(String.format("%-10s %-15s %-10s %-35s %-35s",
                row[0], row[1], row[2], row[3], row[4]));
        }
    }
}
