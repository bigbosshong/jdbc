package hello.jdbc.repository.ex;

public class MyDuplicateKeyException extends RuntimeException {
    public MyDuplicateKeyException() {
    }

    public MyDuplicateKeyException(final String message) {
        super(message);
    }

    public MyDuplicateKeyException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public MyDuplicateKeyException(final Throwable cause) {
        super(cause);
    }
}
