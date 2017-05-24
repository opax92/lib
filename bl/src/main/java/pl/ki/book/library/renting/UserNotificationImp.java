package pl.ki.book.library.renting;

/**
 *
 * @author Seb
 */
class UserNotificationImp implements UserNotification {

    @Override
    public void notifyThatBookIsAvailable(BookFullName bookFullName, UserId firstWaitingUser) {
        //send email or something to user that book is available
    }
}
