package pl.ki.book.library.renting;

/**
 *
 * @author Seb
 */
class RentingBookResult {

    private final boolean isSuccess;
    private final RentingFailureBookResult reason;

    static RentingBookResult createSuccess() {
        return new RentingBookResult(true, null);
    }

    static RentingBookResult createFailure(RentingFailureBookResult reason) {
        return new RentingBookResult(false, reason);
    }

    private RentingBookResult(boolean isSuccess, RentingFailureBookResult reason) {
        this.isSuccess = isSuccess;
        this.reason = reason;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public RentingFailureBookResult getReason() {
        if (isSuccess) {
            throw new IllegalStateException("No reason for success");
        }

        return reason;
    }
}
