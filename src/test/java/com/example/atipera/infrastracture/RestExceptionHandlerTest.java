package com.example.atipera.infrastracture;

import com.example.atipera.infrastructure.RestExceptionHandler;
import com.example.atipera.model.ErrorResponse;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RestExceptionHandlerTest {

    private final RestExceptionHandler restExceptionHandler = new RestExceptionHandler();

    @Test
    public void testHandleWebClientResponseExceptionNotAcceptable() {
        HttpMediaTypeNotAcceptableException exception = Mockito.mock(HttpMediaTypeNotAcceptableException.class);
        HttpStatus status = HttpStatus.NOT_ACCEPTABLE;

        Mockito.when(exception.getStatusCode()).thenReturn(status); // Added this line

        ErrorResponse expectedResponse = new ErrorResponse(status.value(),
                "Not acceptable: 'Accept' header: 'application/xml'. Must accept 'application/json'.");

        ResponseEntity<ErrorResponse> actualResponse = restExceptionHandler.handleWebClientResponseExceptionNotAcceptable(exception);

        assertEquals(expectedResponse.status(), actualResponse.getBody().status());
        assertEquals(expectedResponse.message(), actualResponse.getBody().message());
        assertEquals(status, actualResponse.getStatusCode());
    }

    @Test
    public void testHandleNotExistingUser() {
        HttpStatus status = HttpStatus.NOT_FOUND;
        ErrorResponse expectedResponse = new ErrorResponse(status.value(), "User not found");

        ResponseEntity<?> actualResponse = restExceptionHandler.handleNotExistingUser();

        assertEquals(expectedResponse.status(), ((ErrorResponse)actualResponse.getBody()).status());
        assertEquals(expectedResponse.message(), ((ErrorResponse)actualResponse.getBody()).message());
        assertEquals(status, actualResponse.getStatusCode());
    }

}