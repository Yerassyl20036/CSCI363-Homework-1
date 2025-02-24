
import java.time.LocalDateTime;

import exceptions.BookAlreadyBorrowedException;

public class MutatedLibrary extends Library {

    // Mutation 1: Negate availability check
    public boolean borrowBook_mutated1(String isbn) {
        if (getLoggedInUser() == null || !getLoggedInUser().getRole().equals("MEMBER")) {
            return false;
        }

        Book book = findBook(isbn);
        if (book.isAvailable()) { // Mutated: Should be !book.isAvailable()
            throw new BookAlreadyBorrowedException("Book with ISBN " + isbn + " is already borrowed.");
        }

        if (getLoggedInUser().borrowBook(book)) {
            book.setAvailable(false);
            getTransactions().add(new Transaction(getLoggedInUser(), book));
            return true;
        }

        return false;
    }

    // Mutation 2: Prevent book from becoming unavailable by overriding borrow() method
    public boolean borrowBook_mutated2(String isbn) {
        if (getLoggedInUser() == null || !getLoggedInUser().getRole().equals("MEMBER")) {
            return false;
        }

        Book book = findBook(isbn);
        if (!book.isAvailable()) {
            throw new BookAlreadyBorrowedException("Book with ISBN " + isbn + " is already borrowed.");
        }

        if (getLoggedInUser().borrowBook(new Book(book.getTitle(), book.getAuthor(), book.getISBN(), book.getCondition()) {
            @Override
            public void borrow(String userName) {
                // Mutated: Do not change availability
                getBorrowHistory().add(userName);
            }
        })) {
            getTransactions().add(new Transaction(getLoggedInUser(), book));
            return true;
        }

        return false;
    }

    // Mutation 3: Book remains available after borrowing
    public boolean borrowBook_mutated3(String isbn) {
        if (getLoggedInUser() == null || !getLoggedInUser().getRole().equals("MEMBER")) {
            return false;
        }

        Book book = findBook(isbn);
        if (!book.isAvailable()) {
            throw new BookAlreadyBorrowedException("Book with ISBN " + isbn + " is already borrowed.");
        }

        if (getLoggedInUser().borrowBook(book)) {
            book.setAvailable(true); // Mutated: Should be false, making the book remain available
            getTransactions().add(new Transaction(getLoggedInUser(), book));
            return true;
        }

        return false;
    }

    // Mutation 4: Remove transaction logging
    public boolean borrowBook_mutated4(String isbn) {
        if (getLoggedInUser() == null || !getLoggedInUser().getRole().equals("MEMBER")) {
            return false;
        }

        Book book = findBook(isbn);
        if (!book.isAvailable()) {
            throw new BookAlreadyBorrowedException("Book with ISBN " + isbn + " is already borrowed.");
        }

        if (getLoggedInUser().borrowBook(book)) {
            book.setAvailable(false);
            // Mutated: Removed getTransactions().add(new Transaction(getLoggedInUser(), book));
            return true;
        }

        return false;
    }

    public String returnBook_mutated1(String isbn, LocalDateTime dueDate, boolean isDamaged) {
        if (getLoggedInUser() == null) {
            return "Error: No user logged in.";
        }

        Book book = findBook(isbn);
        for (Transaction transaction : getTransactions()) {
            if (!transaction.isReturned() && transaction.getUser() == getLoggedInUser() && transaction.getBook() == book) {
                transaction.markReturned();
                book.setAvailable(true);

                double lateFee = 10.0; // Mutated: Always apply a late fee
                getLoggedInUser().addFine(lateFee);

                return "Book returned successfully. Late fee: $" + lateFee + ".";
            }
        }
        return "Error: No matching transaction found for this book.";
    }

    public String returnBook_mutated2(String isbn, LocalDateTime dueDate, boolean isDamaged) {
        if (getLoggedInUser() == null) {
            return "Error: No user logged in.";
        }

        Book book = findBook(isbn);
        for (Transaction transaction : getTransactions()) {
            if (!transaction.isReturned() && transaction.getUser() == getLoggedInUser() && transaction.getBook() == book) {
                transaction.markReturned();
                book.setAvailable(true);

                getLoggedInUser().addFine(20.0); // Mutated: Always apply a damage fee

                return "Book returned successfully. Damage fee: $20 applied.";
            }
        }
        return "Error: No matching transaction found for this book.";
    }

    public String returnBook_mutated3(String isbn, LocalDateTime dueDate, boolean isDamaged) {
        if (getLoggedInUser() == null) {
            return "Error: No user logged in.";
        }

        Book book = findBook(isbn);
        for (Transaction transaction : getTransactions()) {
            if (!transaction.isReturned() && transaction.getUser() == getLoggedInUser() && transaction.getBook() == book) {
                transaction.markReturned();

                // Mutated: Removed book.setAvailable(true);
                return "Book returned successfully.";
            }
        }
        return "Error: No matching transaction found for this book.";
    }

    public String returnBook_mutated4(String isbn, LocalDateTime dueDate, boolean isDamaged) {
        if (getLoggedInUser() == null) {
            return "Error: No user logged in.";
        }

        Book book = findBook(isbn);
        for (Transaction transaction : getTransactions()) {
            if (!transaction.isReturned() && transaction.getUser() == getLoggedInUser() && transaction.getBook() == book) {
                transaction.markReturned();
                book.setAvailable(true);

                // Mutated: Removed fine application logic
                return "Book returned successfully.";
            }
        }
        return "Error: No matching transaction found for this book.";
    }

}
