package edu.refactor.demo.exception;

public class ChangeCurrentStatusNoWayException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ChangeCurrentStatusNoWayException() {
    }

    public ChangeCurrentStatusNoWayException(String message) {
        super(message);
    }
}

