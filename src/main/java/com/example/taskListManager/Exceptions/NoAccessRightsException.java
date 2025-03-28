package com.example.taskListManager.Exceptions;

public class NoAccessRightsException extends RuntimeException{
    public NoAccessRightsException (String message){
        super(message);
    }
}
