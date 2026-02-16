package com.abcm.gst_service.exception;

import java.io.Serial;


public class CustomException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    private final int code; 

    public CustomException(String message, int code) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}

