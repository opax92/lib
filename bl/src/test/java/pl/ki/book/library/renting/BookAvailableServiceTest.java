package pl.ki.book.library.renting;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 *
 * @author Seb
 */
public class BookAvailableServiceTest {

    private Map<String, UserId> users = new HashMap<>();
    private Map<String, BookFullName> books = new HashMap<>();
    private MockBookQueueRepository mockBookQueueRepository = new MockBookQueueRepository();
    private BookAvailable bookAvailableService = new BookAvailable(mockBookQueueRepository);
    private MockUserNotification mockBookAvailableNotification = new MockUserNotification();
    private long id ;

    @Test
    public void test(){
        user("Sebastian");
        book("Ksiazka1");
        waitForBookBy("Sebastian").onBook("Ksiazka1").isNotAvailable().waitForBook();

        notifyBookIsAvailable("Ksiazka1");

        assertNotificationSuccessTo("Sebastian", "Ksiazka1");
    }

    @Test(expected = RuntimeException.class)
    public void test2(){
        user("Sebastian");
        book("Ksiazka1");
        waitForBookBy("Sebastian").onBook("Ksiazka1").isAvailable().waitForBook();

        notifyBookIsAvailable("Ksiazka1");

        assertNotificationSuccessTo("Sebastian", "Ksiazka1");
    }

    @Test
    public void test3(){
        user("Sebastian");
        user("Kuba");
        user("Daniel");
        book("Ksiazka1");
        waitForBookBy("Sebastian").onBook("Ksiazka1").isNotAvailable().waitForBook();
        waitForBookBy("Kuba").onBook("Ksiazka1").isNotAvailable().waitForBook();
        waitForBookBy("Daniel").onBook("Ksiazka1").isNotAvailable().waitForBook();

        notifyBookIsAvailable("Ksiazka1");

        assertNotificationSuccessTo("Sebastian", "Ksiazka1");

        notifyBookIsAvailable("Ksiazka1");

        assertNotificationSuccessTo("Kuba", "Ksiazka1");

        notifyBookIsAvailable("Ksiazka1");

        assertNotificationSuccessTo("Daniel", "Ksiazka1");

    }

    private RentConfigurer rent(String bookFullName) {
        return new RentConfigurer(bookFullName);
    }

    private void assertNotificationSuccessTo(String userName, String bookFullName) {
        UserId notifiedBy = mockBookAvailableNotification.isNotifiedBy(books.get(bookFullName));

        assertEquals(users.get(userName), notifiedBy);
    }

    private void notifyBookIsAvailable(String bookFullName) {
        bookAvailableService.notifyBookIsAvailable(books.get(bookFullName), mockBookAvailableNotification);
    }

    private WaitConfigurer waitForBookBy(String userName) {
        return new WaitConfigurer(userName);
    }

    private void user(String userName) {
        users.put(userName, new UserId(generateUniqueId()));
    }

    private void book(String bookFullName) {
        books.put(bookFullName, new BookFullName(bookFullName));
        mockBookQueueRepository.setExists(new BookFullName(bookFullName), true);
    }

    private long generateUniqueId() {
        return id++;
    }

    private class WaitConfigurer {

        private UserId userId;
        private BookFullName bookFullName;

        private WaitConfigurer(String userName) {
            this.userId = users.get(userName);
        }

        private WaitConfigurer onBook(String bookFullName) {
            this.bookFullName = books.get(bookFullName);

            return this;
        }

        private void waitForBook(){
            bookAvailableService.waitWhenBookAvailable(bookFullName, userId);
        }

        public WaitConfigurer isAvailable() {
            mockBookQueueRepository.setAvailable(bookFullName, true);

            return this;
        }

        public WaitConfigurer isNotAvailable() {
            mockBookQueueRepository.setAvailable(bookFullName, false);

            return this;
        }
    }

    private class RentConfigurer {
        public RentConfigurer(String bookFullName) {

        }

        public void by(String kuba) {
        }
    }
}