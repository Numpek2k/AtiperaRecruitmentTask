package com.example.atipera.infrastructure;

import com.example.atipera.model.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@ControllerAdvice
public class RestExceptionHandler{

    @ExceptionHandler(WebClientResponseException.NotAcceptable.class)
    public final ResponseEntity<ErrorResponse> handleWebClientResponseExceptionNotAcceptable(
            HttpMediaTypeNotAcceptableException e) {
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorResponse(e.getStatusCode().value(),"Not acceptable: 'Accept' header: 'application/xml'. Must accept 'application/json'."));
    }

    @ExceptionHandler(WebClientResponseException.NotFound.class)
    public ResponseEntity<?> handleNotExistingUser() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), "User not found"));
    }


}
