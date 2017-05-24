package pl.ki.book.library.renting;


import pl.ki.book.library.renting.exceptions.BookNotExistsException;

import java.util.Optional;

/**
 *
 * @author Seb
 */
//API, the only class with public methods in this bounded context
public class RentingBookService {

    private RentingBookRepository rentingBookRepository;
    private BookNotification bookNotification;
    private UserNotification userNotification;

    public RentingBookService(RentingBookRepository rentingBookRepository, BookNotification bookNotification, UserNotification userNotification) {
        this.rentingBookRepository = rentingBookRepository;
        this.bookNotification = bookNotification;
        this.userNotification = userNotification;
    }

    public RentingBookResult rentBook(UserId userId, BookId bookId) {
        Book book = findBook(bookId);

        RentingBookResult rentingBookResult = book.rentTo(userId);

        store(book);

        return rentingBookResult;
    }

    public RentingBookResult returnBook(UserId userId, BookId bookId) {
        Book book = findBook(bookId);

        RentingBookResult rentingBookResult = book.returnBy(userId);

        if (rentingBookResult.isSuccess()) {
            bookNotification.notifyBookIsAvailable(book.getBookFullName(), userNotification);
        }

        store(book);

        return rentingBookResult;
    }


    private Book findBook(BookId bookId) {
        Book book = rentingBookRepository.findBy(bookId);

        return Optional.ofNullable(book).orElseThrow(BookNotExistsException::new);

    }

    private void store(Book book) {
        rentingBookRepository.store(book);
    }
}
