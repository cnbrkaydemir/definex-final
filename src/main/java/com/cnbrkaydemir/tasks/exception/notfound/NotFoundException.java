package com.cnbrkaydemir.tasks.exception.notfound;

public abstract class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
