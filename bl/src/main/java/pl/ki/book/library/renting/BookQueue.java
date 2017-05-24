package pl.ki.book.library.renting;

import java.util.*;

/**
 *
 * @author Seb
 */
class BookQueue {

    private Queue<UserId> userIds = new LinkedList<>();
    private BookFullName bookFullName;

    public BookQueue(BookFullName bookFullName) {
        this.bookFullName = bookFullName;
    }

    public void addWaitingUser(UserId userId){
        userIds.add(userId);
    }

    public UserId findFirstWaitingUser(){
        return userIds.poll();
    }

    public BookFullName getBookFullName() {
        return bookFullName;
    }
}
