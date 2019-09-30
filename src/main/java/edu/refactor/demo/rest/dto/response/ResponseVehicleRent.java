package edu.refactor.demo.rest.dto.response;

import edu.refactor.demo.entity.status.RentStatusEnum;

import java.io.Serializable;

public class ResponseVehicleRent implements Serializable {
    private static final long serialVersionUID = -735845394256023864L;

    private Long vehicleRentalId;

    private RentStatusEnum status;

    public ResponseVehicleRent() {
    }

    public ResponseVehicleRent(Long vehicleRentalId, RentStatusEnum status) {
        this.vehicleRentalId = vehicleRentalId;
        this.status = status;
    }

    public Long getVehicleRentalId() {
        return vehicleRentalId;
    }

    public void setVehicleRentalId(Long vehicleRentalId) {
        this.vehicleRentalId = vehicleRentalId;
    }

    public RentStatusEnum getStatus() {
        return status;
    }

    public void setStatus(RentStatusEnum status) {
        this.status = status;
    }
}
