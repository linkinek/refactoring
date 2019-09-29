package edu.refactor.demo.exception;

public class VehicleAlreadyTakenException extends RuntimeException {
    private static final long serialVersionUID = 668495192540714622L;

    public VehicleAlreadyTakenException() {
    }

    public VehicleAlreadyTakenException(String message) {
        super(message);
    }
}
