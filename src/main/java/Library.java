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

    public boolean loginUser(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.authenticate(password)) {
                loggedInUser = user;
                return true;
            }
        }
        return false;
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
        if (loggedInUser != null && loggedInUser.getRole().equals("MEMBER")) {
            Book book = findBook(isbn);
            if (!book.isAvailable()) {
                throw new BookAlreadyBorrowedException("Book with ISBN " + isbn + " is already borrowed.");
            }
            if (loggedInUser.borrowBook(book)) {
                transactions.add(new Transaction(loggedInUser, book));
                return true;
            }
        }
        return false;
    }

    public String returnBook(String isbn, LocalDateTime dueDate, boolean isDamaged) {
        if (loggedInUser == null) {
            return "Error: No user logged in.";
        }
    
        Book book = findBook(isbn);
        if (book == null) {
            return "Error: Book not found.";
        }
    
        for (Transaction transaction : transactions) {
            if (!transaction.isReturned() && transaction.getUser() == loggedInUser && transaction.getBook() == book) {
                transaction.markReturned();
                book.setAvailable(true);
    
                double lateFee = transaction.calculateLateFee(dueDate);
                if (lateFee > 0) {
                    loggedInUser.addFine(lateFee);
                }
    
                // New condition: Damage Fee
                if (isDamaged) {
                    loggedInUser.addFine(20.0);
                    return "Book returned successfully, but a damage fee of $20 was charged.";
                }
    
                // New condition: Multiple Late Returns -> Ban User
                if (loggedInUser.getOutstandingFines() > 50) {
                    return "Book returned successfully. You have too many late returns and are now banned.";
                }
    
                return "Book returned successfully.";
            }
        }
        return "Error: No matching transaction found for this book.";
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
