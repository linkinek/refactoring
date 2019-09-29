package edu.refactor.demo.exception;

public class VehicleRentalNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 7332498695345486522L;

    public VehicleRentalNotFoundException() {
    }

    public VehicleRentalNotFoundException(String message) {
        super(message);
    }
}
