package com.example.taskListManager.Exceptions;

public class CommentNotFoundException extends RuntimeException{
    public CommentNotFoundException (String message){
        super(message);
    }
}
