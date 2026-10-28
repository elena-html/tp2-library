package library;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Library {
    private final Map<String, Book> books = new LinkedHashMap<>();
    private final Map<String, Copy> copies = new LinkedHashMap<>();
    private final Map<String, Member> members = new LinkedHashMap<>();
    private final List<Loan> loans = new ArrayList<>();

    public void addBook(Book book) {
        if (book != null) {
            books.put(book.getIsbn(), book);
        }
    }

    public void addCopy(String isbn, String inventoryNumber) {
        Book book = books.get(isbn);
        if (book == null) {
            throw new IllegalArgumentException("Unknown book ISBN: " + isbn);
        }
        copies.put(inventoryNumber, new Copy(inventoryNumber, book));
    }

    public void registerMember(Member member) {
        if (member != null) {
            members.put(member.getNumber(), member);
        }
    }

    public Copy findCopy(String inventoryNumber) {
        Copy copy = copies.get(inventoryNumber);
        if (copy == null) {
            throw new IllegalArgumentException("Unknown inventory number: " + inventoryNumber);
        }
        return copy;
    }

    public Loan lend(String memberNumber, String inventoryNumber, LocalDate today) {
        Member member = members.get(memberNumber);
        if (member == null) {
            throw new IllegalArgumentException("Unknown member: " + memberNumber);
        }

        Copy copy = copies.get(inventoryNumber);
        if (copy == null) {
            throw new IllegalArgumentException("Unknown copy: " + inventoryNumber);
        }

        if (!copy.isAvailable()) {
            throw new IllegalStateException("Copy is not available: " + inventoryNumber);
        }

        long activeLoans = loans.stream()
                .filter(l -> l.getMember().getNumber().equals(memberNumber) && l.isOpen())
                .count();

        if (activeLoans >= 5) {
            throw new IllegalStateException("Member has reached maximum 5 active loans");
        }

        copy.setAvailable(false);
        Loan loan = new Loan(member, copy, today);
        loans.add(loan);
        return loan;
    }

    public void returnCopy(String inventoryNumber, LocalDate today) {
        Copy copy = copies.get(inventoryNumber);
        if (copy == null) {
            throw new IllegalArgumentException("Unknown copy: " + inventoryNumber);
        }

        Loan activeLoan = loans.stream()
                .filter(l -> l.getCopy().getInventoryNumber().equals(inventoryNumber) && l.isOpen())
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No open loan found for copy: " + inventoryNumber));

        activeLoan.returnBook(today);
    }

    /**
     * Searches books by title, ignoring case, and returns all matching books.
     *
     * @param keyword the search keyword
     * @return a List of books matching the keyword
     */
    public List<Book> searchByTitle(String keyword) {
        List<Book> matchingBooks = new ArrayList<>();
        if (keyword == null) {
            return matchingBooks;
        }

        String lowerKeyword = keyword.toLowerCase();
        for (Book book : books.values()) {
            if (book != null && book.getTitle() != null) {
                if (book.getTitle().toLowerCase().contains(lowerKeyword)) {
                    matchingBooks.add(book);
                }
            }
        }
        return matchingBooks;
    }

    public List<Book> getBooks() {
        return new ArrayList<>(books.values());
    }
}
