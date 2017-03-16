package io.github.pivopil.share.exceptions;

import org.springframework.http.HttpStatus;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created on 18.12.16.
 */
public class ExceptionAdapter extends RuntimeException {

    private final String stackTrace;
    private final Exception originalException;

    private final HttpStatus httpStatus;
    private final ErrorCategory errorCategory;
    private final int errorCode;

    public ExceptionAdapter(String message, Error error, HttpStatus httpStatus) {
        this(message, error.code, error.category, httpStatus, null);
    }

    public ExceptionAdapter(String message, Error error, HttpStatus httpStatus, Exception cause) {
        this(message, error.code, error.category, httpStatus, cause);
    }

    public ExceptionAdapter(String message, int errorCode, ErrorCategory errorCategory, HttpStatus httpStatus,
                            Exception cause) {
        super(message, cause);
        this.httpStatus = httpStatus;
        this.errorCategory = errorCategory;
        this.errorCode = errorCode;
        this.stackTrace = cause.toString();
        this.originalException = cause;
    }

    public ExceptionAdapter(Exception e) {
        super(e.toString());
        originalException = e;
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        stackTrace = sw.toString();

        this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        this.errorCategory = ErrorCategory.UNKNOWN;
        this.errorCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
    }

    public void printStackTrace() {
        printStackTrace(System.err);
    }
    public void printStackTrace(java.io.PrintStream s) {
        synchronized(s) {
            s.print(getClass().getName() + ": ");
            s.print(stackTrace);
        }
    }
    public void printStackTrace(PrintWriter s) {
        synchronized(s) {
            s.print(getClass().getName() + ": ");
            s.print(stackTrace);
        }
    }
    public Throwable rethrow() { return originalException; }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public ErrorCategory getErrorCategory() {
        return errorCategory;
    }

    public int getErrorCode() {
        return errorCode;
    }
}