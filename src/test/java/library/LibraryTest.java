package library;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LibraryTest {
    private static final String ISBN = "978-0-00-000001-1";
    private static final LocalDate DAY = LocalDate.of(2026, 10, 1);
    private Library library;

    @BeforeEach
    void setUp() {
        library = new Library();
        library.addBook(new Book(ISBN, "Software Engineering", 2016));
        library.addCopy(ISBN, "C-001");
        library.registerMember(new Member("M-01", "First member"));
    }

    @Test
    void lendingMakesTheCopyUnavailable() {
        Loan loan = library.lend("M-01", "C-001", DAY);
        assertFalse(library.findCopy("C-001").isAvailable());
        assertEquals(DAY.plusDays(14), loan.getDueDate());
    }

    @Test
    void cannotLendUnavailableCopy() {
        library.lend("M-01", "C-001", DAY);
        assertThrows(IllegalStateException.class, () -> library.lend("M-01", "C-001", DAY));
    }

    @Test
    void returnCopyMakesItAvailableAgain() {
        library.lend("M-01", "C-001", DAY);
        library.returnCopy("C-001", DAY.plusDays(5));
        assertTrue(library.findCopy("C-001").isAvailable());
    }

    @Test
    void unknownMemberOrCopyThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> library.lend("UNKNOWN", "C-001", DAY));
        assertThrows(IllegalArgumentException.class, () -> library.lend("M-01", "UNKNOWN", DAY));
    }

    @Test
    void searchByTitleFindsMatchingBooksIgnoringCase() {
        library.addBook(new Book("978-0-00-000002-2", "Clean Code", 2008));
        List<Book> results = library.searchByTitle("software");
        assertEquals(1, results.size());
        assertEquals("Software Engineering", results.get(0).getTitle());

        List<Book> caseResults = library.searchByTitle("CLEAN");
        assertEquals(1, caseResults.size());
        assertEquals("Clean Code", caseResults.get(0).getTitle());
    }

    @Test
    void searchByTitleReturnsEmptyListWhenNotFound() {
        List<Book> results = library.searchByTitle("Unknown");
        assertTrue(results.isEmpty());
    }
}
