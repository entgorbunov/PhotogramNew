package com.photogram.daoException;

public class ServletException extends RuntimeException {
    public ServletException(String message) {
        super(message);
    }


    public ServletException(String message, Throwable cause) {
        super(message, cause);
    }


    public ServletException(Throwable cause) {
        super(cause);
    }
}
