package pl.ki.book.library.renting;

import java.util.HashMap;

/**
 *
 * @author Seb
 */
public class MockNotification implements BookNotification {

    private HashMap<BookFullName, Boolean> bookFullNameBooleanHashMap = new HashMap<>();

    @Override
    public void notifyBookIsAvailable(BookFullName bookFullName, UserNotification userNotification) {
        bookFullNameBooleanHashMap.put(bookFullName, Boolean.TRUE);
    }

    public boolean isNotified(BookFullName bookFullName) {
        Boolean aBoolean = bookFullNameBooleanHashMap.get(bookFullName);
        return aBoolean == Boolean.TRUE;
    }
}
