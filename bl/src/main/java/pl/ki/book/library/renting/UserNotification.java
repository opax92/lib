package pl.ki.book.library.renting;

/**
 *
 * @author Seb
 */
interface UserNotification {

    void notifyThatBookIsAvailable(BookFullName bookFullName, UserId firstWaitingUser);
}