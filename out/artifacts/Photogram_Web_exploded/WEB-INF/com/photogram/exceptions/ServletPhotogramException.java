package com.photogram.exceptions;

public class ServletPhotogramException extends RuntimeException {
    public ServletPhotogramException(String message) {
        super(message);
    }


    public ServletPhotogramException(String message, Throwable cause) {
        super(message, cause);
    }


    public ServletPhotogramException(Throwable cause) {
        super(cause);
    }
}
