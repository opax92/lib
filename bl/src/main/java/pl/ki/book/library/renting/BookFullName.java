package pl.ki.book.library.renting;

import java.util.Objects;

/**
 *
 * @author Seb
 */

class BookFullName {

    private final String bookName;

    public BookFullName(String bookName) {
        this.bookName = bookName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookFullName bookFullName1 = (BookFullName) o;
        return Objects.equals(bookName, bookFullName1.bookName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookName);
    }
}
