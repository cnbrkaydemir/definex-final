package com.cnbrkaydemir.tasks.exception.state;

public abstract class BaseStateException extends IllegalStateException {
    public BaseStateException(String s) {
        super(s);
    }
}
