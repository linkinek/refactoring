package edu.refactor.demo;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Customer implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column
    public String login;
    @Column
    public String email;
    @Column
    public Instant registration;
    @Column
    public String status;
    @Id
    @GeneratedValue
    @Column
    public Long id;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<VehicleRental> rentals = new ArrayList<>();
    @Column
    public String category;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<BillingAccount> billingAccounts;
}
