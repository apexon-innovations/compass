package com.apexon.compass.exception.custom;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UnauthorizedException extends RuntimeException {

    private String message;

    public UnauthorizedException(String message) {
        super();
        this.message = message;
    }

}
