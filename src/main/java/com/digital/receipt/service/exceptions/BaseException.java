package com.digital.receipt.service.exceptions;

import java.io.IOException;

public class BaseException extends IOException {

    public BaseException(String message) {
        super(message);
    }

    @Override
    public Throwable fillInStackTrace() {
        return this;
    }
}
