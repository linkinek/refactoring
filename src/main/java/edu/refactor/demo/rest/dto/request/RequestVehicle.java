package edu.refactor.demo.rest.dto.request;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

public class RequestVehicle implements Serializable {
    private static final long serialVersionUID = -8250685510229687778L;

    @NotNull
    private String title;

    @NotNull
    private BigDecimal price;

    @NotNull
    private String type;

    @NotNull
    private String serialNumber;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSerialNumber() {
        return serialNumber;
    }
}
