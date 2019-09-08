package edu.refactor.demo.rental;

import edu.refactor.demo.customer.Customer;
import edu.refactor.demo.vehicle.Vehicle;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;

@Entity
public class VehicleRental implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue
    @Column
    private Long id;
    @ManyToOne
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
    @Column
    private Instant startRent;
    @Column
    private Instant endRent;
    @Column
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Instant getStartRent() {
        return startRent;
    }

    public void setStartRent(Instant startRent) {
        this.startRent = startRent;
    }

    public Instant getEndRent() {
        return endRent;
    }

    public void setEndRent(Instant endRent) {
        this.endRent = endRent;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
