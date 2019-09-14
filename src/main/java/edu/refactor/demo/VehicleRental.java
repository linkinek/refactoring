package edu.refactor.demo;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;

@Entity
public class VehicleRental implements Serializable {
    public static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue
    @Column
    public Long id;
    @ManyToOne
    @JoinColumn(name = "vehicle_id")
    public Vehicle vehicle;
    @ManyToOne
    @JoinColumn(name = "customer_id")
    public Customer customer;
    @Column
    public Instant startRent;
    @Column
    public Instant endRent;
    @Column
    public String status;
}
