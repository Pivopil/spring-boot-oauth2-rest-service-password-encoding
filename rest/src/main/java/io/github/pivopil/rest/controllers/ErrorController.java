package io.github.pivopil.rest.controllers;

import io.github.pivopil.share.exceptions.ExceptionAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;

/**
 * Created on 21.06.16.
 */
@ControllerAdvice(basePackageClasses = {
        ContentController.class,
        MessageController.class,
        UserController.class,
        HomeController.class
})
public class ErrorController extends ResponseEntityExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(ErrorController.class);

    @ExceptionHandler(ExceptionAdapter.class)
    @ResponseBody
    ResponseEntity<?> handleControllerException(HttpServletRequest request, ExceptionAdapter ex) {
        try {
            throw ex.rethrow();
        } catch (IllegalArgumentException e) {
            log.error("Error type: {}, message: {}", e.getClass(), e.getMessage());
        } catch (Throwable throwable) {
            log.error("Error type: {}, message: {}", throwable.getClass(), throwable.getMessage());
            throwable.printStackTrace();
        }
        HttpStatus status = getStatus(request);
        return new ResponseEntity<>(status.value() + " " + ex.getMessage(), status);
    }

    private HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if (statusCode == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return HttpStatus.valueOf(statusCode);
    }

}