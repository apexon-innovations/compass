package com.apexon.compass.exception.custom;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecordNotFoundException extends RuntimeException {

    String message;

    public RecordNotFoundException(String message) {
        super();
        this.message = message;
    }

}
