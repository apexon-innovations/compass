package com.apexon.compass.exception.custom;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServiceException extends RuntimeException {

    private String message;

    public ServiceException(String message) {
        super();
        this.message = message;
    }

}
