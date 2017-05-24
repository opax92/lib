package pl.ki.book.library.renting;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Seb
 */
class MockRentingBookRepository implements RentingBookRepository {

    private Map<BookId, Book> books = new HashMap<>();
    private Integer count;

    @Override
    public Book findBy(BookId bookId) {
        return books.get(bookId);
    }

    @Override
    public void store(Book book) {
        books.put(book.getBookId(), book);
    }

    @Override
    public Integer count() {
        return books.size();
    }

    @Override
    public Integer rentedBooks() {
        count = 0;
        books.forEach((bookId, book) -> {
            if (book.alreadyRented()) {
                ++count;
            }
        });

        return count;
    }
}
