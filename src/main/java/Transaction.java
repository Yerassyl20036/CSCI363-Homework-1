import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.format.DateTimeFormatter;

public class Transaction {
    private User user;
    private Book book;
    private LocalDateTime borrowDate;
    private LocalDateTime returnDate;
    private static final double LATE_FEE_PER_DAY = 1.0; 

    public Transaction(User user, Book book) {
        this.user = user;
        this.book = book;
        this.borrowDate = LocalDateTime.now();
        this.returnDate = null;
    }

    public User getUser() { return user; }
    public Book getBook() { return book; }

    public void markReturned() {
        this.returnDate = LocalDateTime.now();
    }

    public boolean isReturned() {
        return returnDate != null;
    }

    public double calculateLateFee(LocalDateTime dueDate) {
        if (returnDate == null || returnDate.isBefore(dueDate)) {
            return 0.0; 
        }

        long daysLate = ChronoUnit.DAYS.between(dueDate, returnDate);
        return daysLate * LATE_FEE_PER_DAY;
    }

    public String getDetails() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String borrowTime = borrowDate.format(formatter);
        String returnTime = (returnDate != null) ? returnDate.format(formatter) : "Not Returned Yet";

        return "User: " + user.getUsername() +
               ", Book: " + book.getTitle() +
               ", Borrowed on: " + borrowTime +
               ", Returned on: " + returnTime;
    }
}
