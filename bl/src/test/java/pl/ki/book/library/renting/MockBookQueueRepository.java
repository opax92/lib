package pl.ki.book.library.renting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Seb
 */
public class MockBookQueueRepository implements BookQueueRepository {

    private List<BookQueue> bookQueues = new ArrayList<>();
    private Map<BookFullName, Boolean> availablesBooks = new HashMap<>();
    private Map<BookFullName, Boolean> existingsBooks = new HashMap<>();

    @Override
    public BookQueue findUsersWaitingFor(BookFullName bookFullName) {
        for (BookQueue bookQueue : bookQueues) {
            if (bookQueue.getBookFullName().equals(bookFullName)) {
                return bookQueue;
            }
        }

        return null;
    }

    public void setExists(BookFullName bookFullName, boolean exists) {
        existingsBooks.put(bookFullName, exists);
    }

    public void setAvailable(BookFullName bookFullName, boolean available) {
        availablesBooks.put(bookFullName, available);
    }

    @Override
    public void store(BookQueue bookQueue) {
        bookQueues.add(bookQueue);
    }

    @Override
    public boolean isAvailable(BookFullName bookFullName) {
        return availablesBooks.get(bookFullName);
    }

    @Override
    public boolean isExists(BookFullName bookFullName) {
        return existingsBooks.get(bookFullName);
    }
}
