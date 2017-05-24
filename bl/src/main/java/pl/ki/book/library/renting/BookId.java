package pl.ki.book.library.renting;

import java.util.Objects;

/**
 *
 * @author Seb
 */
class BookId {

    private Long bookId;

    BookId(Long bookId) {
        this.bookId = bookId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookId bookId1 = (BookId) o;
        return Objects.equals(bookId, bookId1.bookId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookId);
    }
}
