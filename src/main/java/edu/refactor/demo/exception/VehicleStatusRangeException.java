package edu.refactor.demo.exception;

public class VehicleStatusRangeException extends RuntimeException {
    private static final long serialVersionUID = 5882448571351875799L;

    public VehicleStatusRangeException() {
    }

    public VehicleStatusRangeException(String message) {
        super(message);
    }
}
