package com.example.team258.common.exception;

import com.example.team258.common.dto.MessageDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({DuplicateUsernameException.class})
    public ResponseEntity<MessageDto> handleException(DuplicateUsernameException ex) {
        MessageDto restApiException = new MessageDto(ex.getMessage());
        return new ResponseEntity<>(
                // HTTP body
                restApiException,
                // HTTP status code
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler({FormNotMatchException.class})
    public ResponseEntity<MessageDto> handleException(FormNotMatchException ex) {
        MessageDto restApiException = new MessageDto(ex.getMessage());
        return new ResponseEntity<>(
                // HTTP body
                restApiException,
                // HTTP status code
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler({TokenNotValidException.class})
    public ResponseEntity<MessageDto> handleException(TokenNotValidException ex) {
        MessageDto restApiException = new MessageDto(ex.getMessage());
        return new ResponseEntity<>(
                // HTTP body
                restApiException,
                // HTTP status code
                HttpStatus.BAD_REQUEST
        );
    }


}