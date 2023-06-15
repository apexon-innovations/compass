package com.apexon.compass.exception.custom;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BadRequestException extends RuntimeException {

    private String message;

    public BadRequestException(String message) {
        super();
        this.message = message;
    }

}
