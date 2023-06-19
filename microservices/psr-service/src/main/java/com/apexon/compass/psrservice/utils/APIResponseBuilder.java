package com.apexon.compass.psrservice.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class APIResponseBuilder {

    private APIResponseBuilder() {

    }

    public static <T> ResponseEntity<T> buildResponse(T t, HttpStatus httpStatus) {

        return ResponseEntity.status(httpStatus).body(t);
    }

}
