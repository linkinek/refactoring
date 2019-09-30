package edu.refactor.demo.rest.dto.request;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

public class RequestVehicleRent implements Serializable {
    private static final long serialVersionUID = -1203922303085594459L;

    @NotNull
    private Long customerId;

    @NotNull
    private Long vehicleId;

    @NotNull
    private Date startDate;

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
}
