package io.github.pivopil.share.exceptions;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created on 18.12.16.
 */
public class ExceptionAdapter extends RuntimeException {
    private final String stackTrace;
    public Exception originalException;
    public ExceptionAdapter(Exception e) {
        super(e.toString());
        originalException = e;
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        stackTrace = sw.toString();
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
}