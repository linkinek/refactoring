package edu.refactor.demo;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;

@Entity
public class BillingAccount implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue
    @Column
    public Long id;
    @Column
    public double money;
    @Column
    public boolean isPrimary;

    @ManyToOne
    @JoinColumn(name = "billing_account_id")
    public Customer customer;
    @Column
    public Instant createdDate;
}
