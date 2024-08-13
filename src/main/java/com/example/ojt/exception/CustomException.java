package com.example.ojt.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
@Getter
@Setter
public class CustomException extends Exception{
    private HttpStatus httpStatus;
    public CustomException(String message, HttpStatus status){
        super(message);
        this.httpStatus = status;
    }
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }
}
