package com.baruchv.exception;

public class TaskNotFoundException extends RuntimeException {

    public TaskNotFoundException(String Uid) {
        super("Could not find task " + Uid);
    }
}

