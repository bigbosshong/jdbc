package hello.jdbc.repository.ex;

public class MyDbException extends RuntimeException {
    public MyDbException() {
    }

    public MyDbException(final String message) {
        super(message);
    }

    public MyDbException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public MyDbException(final Throwable cause) {
        super(cause);
    }
}
