package com.warrier.amajon.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class MyApiException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    public MyApiException(String message) {
        super(message);

    }
}
