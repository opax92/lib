package pl.ki.book.library.renting;

/**
 *
 * @author Seb
 */
interface BookNotification {

    void notifyBookIsAvailable(BookFullName bookFullName, UserNotification userNotification);
}
