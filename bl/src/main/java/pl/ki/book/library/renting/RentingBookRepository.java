package pl.ki.book.library.renting;

/**
 *
 * @author Seb
 */
interface RentingBookRepository {

    Book findBy(BookId bookId);

    void store(Book book);

    Integer count();

    Integer rentedBooks();

}
