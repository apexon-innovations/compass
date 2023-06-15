package com.apexon.compass.exception.custom;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtTokenException extends RuntimeException {

    private String message;

    public JwtTokenException(String message) {
        super();
        this.message = message;
    }

}
