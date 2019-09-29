package edu.refactor.demo.exception;

public class CustomerNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 8807747544874355756L;

    public CustomerNotFoundException() {
    }

    public CustomerNotFoundException(String message) {
        super(message);
    }
}
