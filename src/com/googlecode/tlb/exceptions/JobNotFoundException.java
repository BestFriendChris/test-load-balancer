package com.googlecode.tlb.exceptions;

public class JobNotFoundException extends Exception {
    public JobNotFoundException() {
    }

    public JobNotFoundException(String s) {
        super(s);
    }

    public JobNotFoundException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public JobNotFoundException(Throwable throwable) {
        super(throwable);
    }
}
