package pl.ki.book.library.renting;

import java.util.Optional;

/**
 *
 * @author Seb
 */
public class BookAvailable implements BookNotification {

    private BookQueueRepository bookQueueRepository;

    public BookAvailable(BookQueueRepository bookQueueRepository) {
        this.bookQueueRepository = bookQueueRepository;
    }

    //TODO create BookFullNameDTO to this service
    @Override
    public void notifyBookIsAvailable(BookFullName bookFullName, UserNotification userNotification) {
        Optional<BookQueue> usersWaitingForBook = Optional.ofNullable(bookQueueRepository.findUsersWaitingFor(bookFullName));

        if (usersWaitingForBook.isPresent()) {
            BookQueue bookQueue = usersWaitingForBook.get();
            UserId firstWaitingUser = bookQueue.findFirstWaitingUser();
            userNotification.notifyThatBookIsAvailable(bookFullName, firstWaitingUser);

            store(bookQueue);
        }
    }

    public void waitWhenBookAvailable(BookFullName bookFullName, UserId userId) {
        if (!bookQueueRepository.isExists(bookFullName)) {
            throw new RuntimeException("This book is not exists");
        }

        if (bookQueueRepository.isAvailable(bookFullName)) {
            throw new RuntimeException("Cannot wait for a book, because book is available");
        }

        BookQueue bookQueue = Optional.ofNullable(bookQueueRepository.findUsersWaitingFor(bookFullName)).orElse(new BookQueue(bookFullName));

        bookQueue.addWaitingUser(userId);

        store(bookQueue);
    }

    private void store(BookQueue bookQueue) {
        bookQueueRepository.store(bookQueue);
    }
}
