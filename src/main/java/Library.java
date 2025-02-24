
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import exceptions.BookAlreadyBorrowedException;
import exceptions.BookNotFoundException;

public class Library {

    private List<Book> books;
    private List<User> users;
    private List<Transaction> transactions;
    private User loggedInUser;

    public Library() {
        books = new ArrayList<>();
        users = new ArrayList<>();
        transactions = new ArrayList<>();
        loggedInUser = null;
    }

    public void registerUser(String username, String password, String role, int maxBorrowLimit) {
        users.add(new User(username, password, role, maxBorrowLimit));
    }

    public void loginUser(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.authenticate(password)) {
                loggedInUser = user;
                return;
            }
        }
        throw new IllegalArgumentException("User not found");
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }

    public void logoutUser() {
        loggedInUser = null;
    }

    public boolean addBook(String title, String author, String isbn, String condition) {
        if (loggedInUser != null && loggedInUser.getRole().equals("ADMIN")) {
            books.add(new Book(title, author, isbn, condition));
            return true;
        }
        return false;
    }

    public Book findBook(String isbn) {
        for (Book book : books) {
            if (book.getISBN().equals(isbn)) {
                return book;
            }
        }
        throw new BookNotFoundException("Book with ISBN " + isbn + " not found.");
    }

    public boolean borrowBook(String isbn) {
        if (loggedInUser == null || !loggedInUser.getRole().equals("MEMBER")) {
            return false;
        }

        Book book = findBook(isbn);
        if (!book.isAvailable()) {
            throw new BookAlreadyBorrowedException("Book with ISBN " + isbn + " is already borrowed.");
        }

        if (loggedInUser.borrowBook(book)) {
            book.setAvailable(false);  // Ensure the book is marked as unavailable when borrowed
            transactions.add(new Transaction(loggedInUser, book));
            return true;
        }

        return false;
    }

    public String returnBook(String isbn, LocalDateTime dueDate, boolean isDamaged) {
        if (loggedInUser == null) {
            return "Error: No user logged in.";
        }

        Book book = findBook(isbn);
        boolean transactionExists = false;

        for (Transaction transaction : transactions) {
            if (!transaction.isReturned() && transaction.getUser() == loggedInUser && transaction.getBook() == book) {
                transactionExists = true;
                transaction.markReturned();

                book.setAvailable(true);

                double lateFee = transaction.calculateLateFee(dueDate);
                boolean lateFeeApplied = lateFee > 0;
                boolean damageFeeApplied = isDamaged;

                if (lateFeeApplied) {
                    loggedInUser.addFine(lateFee);
                }
                if (damageFeeApplied) {
                    loggedInUser.addFine(20.0);
                }

                boolean isBanned = loggedInUser.getOutstandingFines() > 50;

                // Build return message dynamically
                StringBuilder response = new StringBuilder("Book returned successfully.");
                if (lateFeeApplied) {
                    response.append(" Late fee: $").append(lateFee).append(".");
                }
                if (damageFeeApplied) {
                    response.append(" Damage fee: $20 applied.");
                }
                if (isBanned) {
                    response.append(" You have too many late returns and are now banned.");
                }

                return response.toString();
            }
        }

        if (!transactionExists) {
            book.setAvailable(true);
            return "Error: No matching transaction found for this book.";
        }

        return "Unexpected error: Book return process failed.";
    }

    public String reserveBook(String isbn) {
        if (loggedInUser == null) {
            return "Error: No user logged in.";
        }

        Book book = findBook(isbn);
        if (book == null) {
            return "Error: Book not found.";
        }

        if (!book.isAvailable()) {
            return "Error: Book is already borrowed.";
        }

        if (loggedInUser.getBorrowedBooks().size() >= loggedInUser.getMaxBorrowLimit()) {
            return "Error: Borrow limit reached.";
        }

        transactions.add(new Transaction(loggedInUser, book));
        book.setAvailable(false);
        return "Book reserved successfully.";
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public List<Book> getBooks() {
        return books;
    }
}
