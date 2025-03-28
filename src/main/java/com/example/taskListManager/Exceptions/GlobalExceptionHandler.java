package com.example.taskListManager.Exceptions;

import com.example.taskListManager.DTO.ExceptionDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionDTO> handleValidationException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining("; "));

        log.error("Ошибка валидации: {}", message);
        return new ResponseEntity<>(new ExceptionDTO(message), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionDTO> catchGlobalException(Exception e) {
        log.error("Unexpected error: ", e);
        return new ResponseEntity<>(new ExceptionDTO("Internal server error"), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ExceptionDTO> catchUserNotFoundException(UserNotFoundException e){
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new ExceptionDTO(e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<ExceptionDTO> catchTaskNotFoundException(TaskNotFoundException e){
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new ExceptionDTO(e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CommentNotFoundException.class)
    public ResponseEntity<ExceptionDTO> catchCommentNotFoundException(CommentNotFoundException e){
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new ExceptionDTO(e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IncorrectEmailOrPasswordException.class)
    public ResponseEntity<ExceptionDTO> catchIncorrectEmailOrPasswordException(IncorrectEmailOrPasswordException e){
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new ExceptionDTO(e.getMessage()), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(NoAccessRightsException.class)
    public ResponseEntity<ExceptionDTO> catchNoAccessRightsException(NoAccessRightsException e){
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new ExceptionDTO(e.getMessage()), HttpStatus.FORBIDDEN);
    }
}
