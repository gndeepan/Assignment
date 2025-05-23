package com.assignment.task.Assignment.exception;

public class TaskNotFoundException extends RuntimeException {

    public TaskNotFoundException(String message) {
        super(message);
    }
}