package com.apexon.compass.exception.custom;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class EncryptionException extends RuntimeException {

    private String message;

    public EncryptionException(String message) {
        this.message = message;
    }

}
