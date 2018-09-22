/*
 * This code is property of Robust Wealth.  Please do not share.
 */
package net.pacnode.spring.exception;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * @author JoseMartinez
 */
@ControllerAdvice
public class HttpErrorExceptionHandler extends ResponseEntityExceptionHandler {

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @ExceptionHandler(value = {HttpErrorException.class})
    protected ResponseEntity<Object> handleConflict(HttpErrorException ex, WebRequest request) {
        Throwable cause = ex.getCause();
        logger.error("Converting HttpErrorException exception to proper HTTP response.", ex);
        return handleExceptionInternal(
                ex,
                gson.toJson(new ErrorMsg(
                        ex.httpStatus.value(),
                        ex.getMessage(),
                        cause == null ? "null" : cause.getMessage())),
                new HttpHeaders(),
                ex.httpStatus,
                request);
    }

    static class ErrorMsg {

        int status;
        String message;
        String cause;

        private ErrorMsg(int status, String message, String cause) {
            this.status = status;
            this.message = message;
            this.cause = cause;
        }
    }

}
