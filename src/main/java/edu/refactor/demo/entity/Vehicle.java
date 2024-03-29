package edu.refactor.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.refactor.demo.entity.status.VehicleStatusEnum;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Vehicle implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column
    private Long id;

    @NotNull
    @Column
    private String serialNumber;

    @Column
    private String title;

    @Column
    @NotNull
    private BigDecimal price;

    @Column(name = "STATUS")
    @NotNull
    private String status;

    @Column
    private String type;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "vehicle", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VehicleRental> rentals = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public VehicleStatusEnum getStatus() {
        return status == null ? null : VehicleStatusEnum.fromId(status);
    }

    public void setStatus(VehicleStatusEnum rentStatus) {
        this.status = rentStatus == null ? null : rentStatus.getId();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<VehicleRental> getRentals() {
        return rentals;
    }

    public void setRentals(List<VehicleRental> rentals) {
        this.rentals = rentals;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }
}
