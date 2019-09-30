package edu.refactor.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.refactor.demo.entity.status.CategoryEnum;
import edu.refactor.demo.entity.status.CustomerStatusEnum;
import edu.refactor.demo.entity.status.RentStatusEnum;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Customer implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column
    private String login;

    @Column
    private String email;

    @Column
    private Instant registration;

    @Column(name = "STATUS")
    private String status;

    @Id
    @GeneratedValue
    @Column
    private Long id;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VehicleRental> rentals = new ArrayList<>();

    @Column
    private String category;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BillingAccount> billingAccounts;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Instant getRegistration() {
        return registration;
    }

    public void setRegistration(Instant registration) {
        this.registration = registration;
    }

    public CustomerStatusEnum getStatus() {
        return status == null ? null : CustomerStatusEnum.fromId(status);
    }

    public void setStatus(CustomerStatusEnum status) {
        this.status = status == null ? null : status.getId();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<VehicleRental> getRentals() {
        return rentals;
    }

    public void setRentals(List<VehicleRental> rentals) {
        this.rentals = rentals;
    }

    public CategoryEnum getCategory() {
        return category == null ? null : CategoryEnum.fromId(category);
    }

    public void setCategory(CategoryEnum category) {
        this.category = category == null ? null : category.getId();
    }

    public List<BillingAccount> getBillingAccounts() {
        return billingAccounts;
    }

    public void setBillingAccounts(List<BillingAccount> billingAccounts) {
        this.billingAccounts = billingAccounts;
    }
}
