package edu.refactor.demo.exception;

public class VehicleNotFoundException extends RuntimeException {
    private static final long serialVersionUID = -2902822497474431140L;

    public VehicleNotFoundException() {
    }

    public VehicleNotFoundException(String message) {
        super(message);
    }
}
