/*
 * This code is property of Robust Wealth.  Please do not share.
 */
package net.pacnode.spring.exception;

import org.springframework.http.HttpStatus;

/**
 * @author JoseMartinez
 */
public class HttpErrorException extends RuntimeException {

    HttpStatus httpStatus;

    public HttpErrorException(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public HttpErrorException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpErrorException(HttpStatus httpStatus, String message, Throwable cause) {
        super(message, cause);
        this.httpStatus = httpStatus;
    }

    public HttpErrorException(HttpStatus httpStatus, Throwable cause) {
        super(cause);
        this.httpStatus = httpStatus;
    }

    public HttpErrorException(HttpStatus httpStatus, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.httpStatus = httpStatus;
    }

    public static void throwIfNull(Object object, HttpStatus httpStatus, String msg) {
        if (object == null)
            throw new HttpErrorException(httpStatus, msg);
    }

    public static void throwConflictIfNotNull(Object object, String msg) {
        if (object != null)
            throw new HttpErrorException(HttpStatus.CONFLICT, msg);
    }

    public static void throwIfBlank(String field, HttpStatus httpStatus, String msg) {
        if (field == null || field.isEmpty())
            throw new HttpErrorException(httpStatus, msg);
    }

    public static void throwBadRequestIfBlank(String field, String msg) {
        if (field == null || field.isEmpty())
            throw new HttpErrorException(HttpStatus.BAD_REQUEST, msg);
    }

    public static void throwIfTrue(boolean condition, HttpStatus httpStatus, String msg) {
        if (condition)
            throw new HttpErrorException(httpStatus, msg);
    }

    public static void throwIfFalse(boolean condition, HttpStatus httpStatus, String msg) {
        if (!condition)
            throw new HttpErrorException(httpStatus, msg);
    }

}
