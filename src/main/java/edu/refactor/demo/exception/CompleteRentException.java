package edu.refactor.demo.exception;

public class CompleteRentException extends RuntimeException {
    private static final long serialVersionUID = 5928403659272771628L;

    public CompleteRentException() {
    }

    public CompleteRentException(String message) {
        super(message);
    }
}
