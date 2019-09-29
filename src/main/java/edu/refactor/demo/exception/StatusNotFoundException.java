package edu.refactor.demo.exception;

public class StatusNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public StatusNotFoundException() {
    }

    public StatusNotFoundException(String message) {
        super(message);
    }
}
