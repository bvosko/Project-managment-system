package com.baruchv.exception;

public class NotFoundParent extends RuntimeException{
    public NotFoundParent(String Uid) {
        super("Could not find parent project " + Uid);
    }
}
