package pl.ki.book.library.renting;

import java.util.HashMap;

/**
 *
 * @author Seb
 */
public class MockUserNotification implements UserNotification {

    private HashMap<BookFullName, UserId> bookFullNameBooleanHashMap = new HashMap<>();

    @Override
    public void notifyThatBookIsAvailable(BookFullName bookFullName, UserId firstWaitingUser) {
        bookFullNameBooleanHashMap.put(bookFullName, firstWaitingUser);
    }

    public UserId isNotifiedBy(BookFullName bookFullName) {
        return bookFullNameBooleanHashMap.get(bookFullName);
    }
}
