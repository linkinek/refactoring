package edu.refactor.demo;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Vehicle implements Serializable {
    public static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue
    @Column
    public Long id;
    @Column
    public String title;
    @Column
    public double price;
    @Column
    public String status;
    @Column
    public String type;
    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "vehicle", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<VehicleRental> rentals = new ArrayList<>();
    @Column
    public String serialNumber;
}
