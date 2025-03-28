package com.example.taskListManager.Exceptions;

public class TaskNotFoundException extends RuntimeException{
    public TaskNotFoundException (String message){
        super(message);
    }
}
