package library;

import java.time.LocalDate;
import java.util.Objects;

public class Loan {
    private final Member member;
    private final Copy copy;
    private final LocalDate startDate;
    private final LocalDate dueDate;
    private LocalDate returnDate;

    public Loan(Member member, Copy copy, LocalDate startDate) {
        this.member = Objects.requireNonNull(member, "Member cannot be null");
        this.copy = Objects.requireNonNull(copy, "Copy cannot be null");
        this.startDate = Objects.requireNonNull(startDate, "Start date cannot be null");
        this.dueDate = startDate.plusDays(14);
    }

    public Member getMember() {
        return member;
    }

    public Copy getCopy() {
        return copy;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void returnBook(LocalDate returnDate) {
        this.returnDate = returnDate;
        this.copy.setAvailable(true);
    }

    public boolean isOpen() {
        return returnDate == null;
    }

    public boolean isLate(LocalDate today) {
        return isOpen() && today.isAfter(dueDate);
    }
}
