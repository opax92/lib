package pl.ki.book.library.renting;

import org.junit.Before;
import org.junit.Test;
import pl.ki.book.library.renting.exceptions.BookNotExistsException;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 *
 * @author Seb
 */
public class RentingBookServiceTest {

    private Map<String, UserId> users = new HashMap<>();
    private Map<String, BookId> books = new HashMap<>();
    private MockRentingBookRepository mockRentingBookRepository;
    private MockNotification mockNotificationService;
    private RentingBookService rentingBookService;
    private RentingBookResult rentingBookResult;
    private long id;

    @Before
    public void setUp() throws Exception {
        MockUserNotification mockBookAvailableNotification = new MockUserNotification();
        mockRentingBookRepository = new MockRentingBookRepository();
        mockNotificationService = new MockNotification();
        this.rentingBookService = new RentingBookService(mockRentingBookRepository, mockNotificationService, mockBookAvailableNotification);
    }

    @Test
    public void rentOneBookByOneUser(){
        user("Sebastian");
        book("Ksiazka1");

        rentOneBook("Ksiazka1").by("Sebastian").rentBook();

        assertIsSuccess();
        assertAvailableBooks(0);
        assertRentedBooks(1);
        assertBook("Ksiazka1").isRented().by("Sebastian");
    }

    @Test(expected = BookNotExistsException.class)
    public void rentNotExistsBook(){
        user("Sebastian");
        book("Ksiazka1");

        rentOneBook("Ksiazka1sdf").by("Sebastian").rentBook();
    }

    @Test
    public void rentMoreBooksByOneUser(){
        user("Sebastian");
        book("Ksiazka1");
        book("Ksiazka2");
        book("Ksiazka3");

        rentOneBook("Ksiazka1").by("Sebastian").rentBook();
        rentOneBook("Ksiazka2").by("Sebastian").rentBook();
        rentOneBook("Ksiazka3").by("Sebastian").rentBook();

        assertIsSuccess();
        assertAvailableBooks(0);
        assertRentedBooks(3);
        assertBook("Ksiazka1").isRented().by("Sebastian");
        assertBook("Ksiazka2").isRented().by("Sebastian");
        assertBook("Ksiazka3").isRented().by("Sebastian");
    }

    @Test
    public void rentOneTheSameBookByOneUserTwice(){
        user("Sebastian");
        book("Ksiazka1");
        rentOneBook("Ksiazka1").by("Sebastian").rentBook();

        rentOneBook("Ksiazka1").by("Sebastian").rentBook();

        assertIsFailure(RentingFailureBookResult.ALREADY_RENTED);
        assertAvailableBooks(0);
        assertRentedBooks(1);
        assertBook("Ksiazka1").isRented().by("Sebastian");
    }

    @Test
    public void rentBookWhenAlreadyRented(){
        user("Sebastian");
        user("Tomek");
        book("Ksiazka1");
        rentOneBook("Ksiazka1").by("Sebastian").rentBook();

        rentOneBook("Ksiazka1").by("Tomek").rentBook();

        assertIsFailure(RentingFailureBookResult.ALREADY_RENTED);
        assertAvailableBooks(0);
        assertRentedBooks(1);
        assertBook("Ksiazka1").isRented().by("Sebastian");
    }

    @Test
    public void rentMoreBooksByDifferentUsers(){
        user("Sebastian");
        user("Kuba");
        user("Tomek");
        book("Ksiazka1");
        book("Ksiazka2");
        book("Ksiazka3");
        book("Ksiazka4");

        rentOneBook("Ksiazka1").by("Sebastian").rentBook();
        rentOneBook("Ksiazka2").by("Kuba").rentBook();
        rentOneBook("Ksiazka3").by("Tomek").rentBook();

        assertIsSuccess();
        assertAvailableBooks(1);
        assertRentedBooks(3);
        assertBook("Ksiazka1").isRented().by("Sebastian");
        assertBook("Ksiazka2").isRented().by("Kuba");
        assertBook("Ksiazka3").isRented().by("Tomek");
    }

    @Test
    public void returnOneBook(){
        user("Sebastian");
        book("Ksiazka1");
        rentOneBook("Ksiazka1").by("Sebastian").rentBook();

        returnBook("Ksiazka1").by("Sebastian").returnBook();

        assertIsSuccess();
        assertNotificationBookAvailable("Ksiazka1");
        assertAvailableBooks(1);
        assertRentedBooks(0);
    }

    @Test
    public void returnOneBookWithBadUser(){
        user("Sebastian");
        user("Tomek");
        book("Ksiazka1");
        rentOneBook("Ksiazka1").by("Sebastian").rentBook();

        returnBook("Ksiazka1").by("Tomek").returnBook();

        assertIsFailure(RentingFailureBookResult.RETURN_BY_WRONG_USER);
        assertNotNotificationBookAvailable("Ksiazka1");
        assertAvailableBooks(0);
        assertRentedBooks(1);
        assertBook("Ksiazka1").isRented().by("Sebastian");
    }

    @Test(expected = BookNotExistsException.class)
    public void returnBookNotExists(){
        user("Sebastian");
        book("Ksiazka1");

        returnBook("Ksiazka15").by("Sebastian").returnBook();
        assertNotNotificationBookAvailable("Ksiazka1");
    }

    @Test
    public void returnTheSameBookBooksTwice(){
        user("Sebastian");
        book("Ksiazka1");
        rentOneBook("Ksiazka1").by("Sebastian").rentBook();

        returnBook("Ksiazka1").by("Sebastian").returnBook();
        returnBook("Ksiazka1").by("Sebastian").returnBook();

        assertIsFailure(RentingFailureBookResult.NOT_RENTED);
        assertNotificationBookAvailable("Ksiazka1");
        assertAvailableBooks(1);
        assertRentedBooks(0);
    }

    @Test
    public void returnMoreBooksOk(){
        user("Sebastian");
        user("Tomek");
        user("Kasia");
        book("Ksiazka1");
        book("Ksiazka2");
        book("Ksiazka3");
        book("Ksiazka4");
        rentOneBook("Ksiazka1").by("Sebastian").rentBook();
        rentOneBook("Ksiazka2").by("Tomek").rentBook();

        returnBook("Ksiazka1").by("Sebastian").returnBook();

        assertIsSuccess();
        assertAvailableBooks(3);
        assertNotificationBookAvailable("Ksiazka1");
        assertNotNotificationBookAvailable("Ksiazka2");
        assertRentedBooks(1);
        assertBook("Ksiazka2").isRented().by("Tomek");
    }

    @Test
    public void returnMoreBooksNotOk(){
        user("Sebastian");
        user("Tomek");
        user("Kasia");
        book("Ksiazka1");
        book("Ksiazka2");
        book("Ksiazka3");
        book("Ksiazka4");
        rentOneBook("Ksiazka1").by("Sebastian").rentBook();
        rentOneBook("Ksiazka2").by("Tomek").rentBook();

        returnBook("Ksiazka1").by("Tomek").returnBook();

        assertIsFailure(RentingFailureBookResult.RETURN_BY_WRONG_USER);
        assertNotNotificationBookAvailable("Ksiazka1");
        assertAvailableBooks(2);
        assertRentedBooks(2);
        assertBook("Ksiazka1").isRented().by("Sebastian");
        assertBook("Ksiazka2").isRented().by("Tomek");
    }

    private void user(String userName) {
        users.put(userName, new UserId(generateUniqueId()));
    }

    private void book(String booFullName) {
        BookId bookId = new BookId(generateUniqueId());
        books.put(booFullName, bookId);
        mockRentingBookRepository.store(new Book(bookId, new BookFullName(booFullName)));
    }

    private void assertIsSuccess() {
        assertTrue(rentingBookResult.isSuccess());
    }

    private RentBookConfigurer returnBook(String bookName) {
        return new RentBookConfigurer(bookName);
    }

    private void assertIsFailure(RentingFailureBookResult rentingFailureBookResult) {
        assertEquals(rentingFailureBookResult, rentingBookResult.getReason());
    }

    private BookAssertion assertBook(String bookId) {
        return new BookAssertion(bookId);
    }

    private void assertRentedBooks(Integer expected) {
        assertEquals(expected, mockRentingBookRepository.rentedBooks());
    }

    private void assertAvailableBooks(int expected) {
        assertEquals(expected, mockRentingBookRepository.count() - mockRentingBookRepository.rentedBooks());
    }

    private void assertNotificationBookAvailable(String bookFullName) {
        assertTrue(mockNotificationService.isNotified(new BookFullName(bookFullName)));
    }

    private void assertNotNotificationBookAvailable(String bookFullName) {
        assertFalse(mockNotificationService.isNotified(new BookFullName(bookFullName)));
    }

    private long generateUniqueId() {
        return id++;
    }

    private RentBookConfigurer rentOneBook(String bookName) {
        return new RentBookConfigurer(bookName);
    }

    private class RentBookConfigurer {

        private UserId userId;
        private BookId bookId;

        private RentBookConfigurer(String bookName) {
            bookId = books.get(bookName);
        }

        private RentBookConfigurer by(String userName) {
            userId = users.get(userName);

            return this;
        }

        private void rentBook() {
            rentingBookResult = rentingBookService.rentBook(userId, bookId);
        }


        private void returnBook() {
            rentingBookResult = rentingBookService.returnBook(userId, bookId);
        }
    }

    private class BookAssertion {

        private BookId bookId;
        private Book book;

        private BookAssertion(String bookName) {
            bookId = books.get(bookName);
            book = mockRentingBookRepository.findBy(bookId);
        }

        private BookAssertion isRented() {
            assertTrue(book.alreadyRented());

            return this;
        }

        private void by(String userName) {
            UserId userId = users.get(userName);
            assertEquals(userId, book.getRentedBy());
        }
    }
}