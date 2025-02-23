import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        Library library = new Library();

        // Registering users with roles
        library.registerUser("Alice", "password123", "MEMBER", 2);
        library.registerUser("Bob", "password456", "MEMBER", 1);
        library.registerUser("AdminUser", "adminpass", "ADMIN", 5);

        // Logging in as an admin to add books
        library.loginUser("AdminUser", "adminpass");
        library.addBook("Java Programming", "John Doe", "1234", "New");
        library.addBook("Data Structures", "Jane Smith", "5678", "Used");
        library.logoutUser();

        // Logging in as Alice to borrow and return books
        library.loginUser("Alice", "password123");
        library.borrowBook("1234");
        library.logoutUser();

        // Logging in as Bob to borrow a book
        library.loginUser("Bob", "password456");
        library.borrowBook("5678");
        library.logoutUser();

        // Logging in as Alice to return her book
        library.loginUser("Alice", "password123");

        // Fix: Provide a due date (assume due date is 10 days after borrowing)
        LocalDateTime dueDate = LocalDateTime.now().minusDays(10);  
        library.returnBook("1234", dueDate);

        library.logoutUser();

        // Printing transactions
        for (Transaction transaction : library.getTransactions()) {
            System.out.println(transaction.getDetails());
        }
    }
}
