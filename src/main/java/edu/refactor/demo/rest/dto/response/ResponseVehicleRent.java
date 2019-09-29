package edu.refactor.demo.rest.dto.response;

import java.io.Serializable;

public class ResponseVehicleRent implements Serializable {
    private static final long serialVersionUID = -735845394256023864L;

    private Long vehicleRentalId;

    private String status;

    public ResponseVehicleRent() {
    }

    public ResponseVehicleRent(Long vehicleRentalId, String status) {
        this.vehicleRentalId = vehicleRentalId;
        this.status = status;
    }

    public Long getVehicleRentalId() {
        return vehicleRentalId;
    }

    public void setVehicleRentalId(Long vehicleRentalId) {
        this.vehicleRentalId = vehicleRentalId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
