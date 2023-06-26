package com.apexon.compass.exception.custom;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ForbiddenException extends RuntimeException {

    private String message;

    public ForbiddenException(String message) {
        super();
        this.message = message;
    }

}
