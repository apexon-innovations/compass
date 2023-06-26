package com.apexon.compass.exception.custom;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TypeConversionException extends RuntimeException {

    String message;

    public TypeConversionException(String message) {
        super();
        this.message = message;
    }

}
