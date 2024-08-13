package com.example.ojt.advice;

import com.example.ojt.exception.*;
import com.example.ojt.model.dto.response.ResponseError;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class APIControllerAdvice {
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Map<String,Object> forbidden(AccessDeniedException e){
        Map<String,Object> map = new HashMap<>();
        map.put("error",new ResponseError(403,"FOR_BIDDEN",e));
        return  map;
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, Object> invalidRequest(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(fieldError -> {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        });

        Map<String, Object> response = new HashMap<>();
        response.put("error", "Validation Failed");
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("message", errors);
        return response;
    }

    @ExceptionHandler(CustomException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, Object> customException(CustomException c) {
        Map<String, Object> map = new HashMap<>();
        map.put("error" ,new ResponseError(c.getHttpStatus().value(), c.getHttpStatus().name(),c.getMessage()));
        return map;
    }



    @ExceptionHandler(NotFoundException.class)
    public Map<String, Object> handleNotFoundException(NotFoundException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("message", ex.getMessage());
        errorResponse.put("status", "NOT_FOUND");
        return errorResponse;
    }

    @ExceptionHandler(AccountLockedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String,Object> handelAccountLockedException(AccountLockedException e){
        Map<String,Object> map = new HashMap<>();
        map.put("error", new ResponseError(403,"FOR_BIDDEN",e.getMessage()));
        return map;
    }

    @ExceptionHandler(RequestErrorException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String,Object> handelRequestErrorException(RequestErrorException e){
        Map<String,Object> map = new HashMap<>();
        map.put("error", new ResponseError(400,"BAD_REQUEST",e.getMessage()));
        return map;
    }
}
