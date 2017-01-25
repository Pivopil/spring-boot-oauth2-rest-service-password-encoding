package io.github.pivopil.share.exceptions;

import java.util.List;
import java.util.Map;

/**
 * Created on 24.01.17.
 */
public class CustomOvalException extends RuntimeException  {

    private final Map<String, List<String>> errorMap;

    public CustomOvalException(Map<String, List<String>> errorMap) {
        super("Map with Oval validation exceptions");
        this.errorMap = errorMap;
    }

    public Map<String, List<String>> getErrorMap() {
        return errorMap;
    }
}
