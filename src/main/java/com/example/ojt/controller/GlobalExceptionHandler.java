package com.example.ojt.controller;

import com.example.ojt.exception.CustomException;
import com.example.ojt.model.dto.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
//        List<Map<String, String>> errors = ex.getBindingResult().getAllErrors().stream()
//                .map(error -> {
//                    String fieldName = ((FieldError) error).getField();
//                    String errorMessage = error.getDefaultMessage();
//                    Map<String, String> errorDetails = new HashMap<>();
//                    errorDetails.put("field", fieldName);
//                    errorDetails.put("message", errorMessage);
//                    return errorDetails;
//                }).collect(Collectors.toList());
//
//        Map<String, Object> response = new HashMap<>();
//        response.put("error", "Validation Failed");
//        response.put("messages", errors);
//        response.put("status", HttpStatus.BAD_REQUEST.value());
//
//        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
//    }
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<?> handleCustomException(CustomException ex) {
        HttpStatus status = ex.getHttpStatus();
        return ResponseEntity.status(status).body(new ErrorResponse(status.value(), ex.getMessage(), status.getReasonPhrase()));
    }
}
