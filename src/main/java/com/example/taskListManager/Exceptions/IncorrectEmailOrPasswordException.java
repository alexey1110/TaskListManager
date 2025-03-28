package com.example.taskListManager.Exceptions;

public class IncorrectEmailOrPasswordException extends RuntimeException{
    public IncorrectEmailOrPasswordException (String message){
        super(message);
    }
}
