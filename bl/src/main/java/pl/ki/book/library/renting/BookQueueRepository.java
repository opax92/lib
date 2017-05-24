package pl.ki.book.library.renting;

/**
 *
 * @author Seb
 */
interface BookQueueRepository {

    BookQueue findUsersWaitingFor(BookFullName bookFullName);

    boolean isAvailable(BookFullName bookFullName);

    boolean isExists(BookFullName bookFullName);

    void store(BookQueue bookQueue);

}
