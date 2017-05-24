package pl.ki.book.library.renting;

import java.util.Optional;

/**
 *
 * @author Seb
 */
class Book {

    private BookId bookId;
    private UserId rentedBy;
    private BookFullName bookFullName;

    public Book(BookId bookId, BookFullName bookFullName) {
        this.bookId = bookId;
        this.bookFullName = bookFullName;
    }

    RentingBookResult rentTo(UserId userId) {
        if (alreadyRented()) {
            return RentingBookResult.createFailure(RentingFailureBookResult.ALREADY_RENTED);
        }

        assignBookTo(userId);
        return RentingBookResult.createSuccess();
    }

    RentingBookResult returnBy(UserId userId) {
        if (!alreadyRented()) {
            return RentingBookResult.createFailure(RentingFailureBookResult.NOT_RENTED);
        }

        if (isNotReturnByTheSameUserWhichRent(userId)) {
            return RentingBookResult.createFailure(RentingFailureBookResult.RETURN_BY_WRONG_USER);
        }

        returnBook();

        return RentingBookResult.createSuccess();
    }

    BookId getBookId() {
        return bookId;
    }

    UserId getRentedBy() {
        return Optional.ofNullable(rentedBy).orElseThrow(RuntimeException::new);
    }

    BookFullName getBookFullName() {
        return bookFullName;
    }

    boolean alreadyRented() {
        return rentedBy != null;
    }

    private boolean isNotReturnByTheSameUserWhichRent(UserId returnUser) {
        return !returnUser.equals(rentedBy);
    }

    private void returnBook() {
        rentedBy = null;
    }

    private void assignBookTo(UserId userId) {
        rentedBy = userId;
    }
}
