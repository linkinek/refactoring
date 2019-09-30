package edu.refactor.demo.exception;

public class CustomerAlreadyExistsException extends RuntimeException {
    private static final long serialVersionUID = 8807747544874355756L;

    public CustomerAlreadyExistsException() {
    }

    public CustomerAlreadyExistsException(String message) {
        super(message);
    }
}
