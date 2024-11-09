package com.skthakur.bgn_backend.util;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.Map;

@Setter
@Getter
public class ResponseWrapper<T> {
    private HttpStatus status;
    private String message;
    private T data;
    private Map<String, String> errors;

    public ResponseWrapper(HttpStatus status, String message, T data, Map<String, String> errors) {
        this.status = status;
        this.message = message;
        this.data = data;
        this.errors = errors;
    }

}


