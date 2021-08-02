package com.digital.receipt.common.exceptions;

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
