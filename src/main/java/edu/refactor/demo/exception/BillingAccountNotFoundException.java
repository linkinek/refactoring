package edu.refactor.demo.exception;

public class BillingAccountNotFoundException extends RuntimeException {

    private static final long serialVersionUID = -6186612774987104215L;

    public BillingAccountNotFoundException() {
    }

    public BillingAccountNotFoundException(String message) {
        super(message);
    }
}
