package edu.refactor.demo.service.util;

import edu.refactor.demo.entity.BillingAccount;
import edu.refactor.demo.entity.Customer;
import edu.refactor.demo.entity.Vehicle;
import edu.refactor.demo.entity.VehicleRental;
import edu.refactor.demo.entity.currency.Currency;
import edu.refactor.demo.entity.currency.soup.model.CurrencyType;
import edu.refactor.demo.entity.status.CustomerStatusEnum;
import edu.refactor.demo.entity.status.RentStatusEnum;
import edu.refactor.demo.entity.status.VehicleStatusEnum;

import java.math.BigDecimal;

public class EntityMockBuilder {

    @SuppressWarnings("unchecked")
    public static <B extends EntityMockBuild> B mockTo(Class entityClass) {
        if (Vehicle.class.equals(entityClass)) {
            return (B) new VehicleMockBuilder();
        }

        if(VehicleRental.class.equals(entityClass)){
            return (B) new VehicleRentalMockBuilder();
        }

        if(BillingAccount.class.equals(entityClass)){
            return (B) new BillingAccountMockBuilder();
        }

        if(Customer.class.equals(entityClass)){
            return (B) new CustomerMockBuilder();
        }

        if(Currency.class.equals(entityClass)){
            return (B) new CurrencyMockBuilder();
        }

        throw new IllegalArgumentException(entityClass.getSimpleName() + "MockBuilder not found");
    }

    interface EntityMockBuild<T> {
        T build();
    }

    public static class VehicleMockBuilder implements EntityMockBuild<Vehicle> {
        Vehicle vehicle;

        private VehicleMockBuilder() {
            this.vehicle = new Vehicle();
        }

        public VehicleMockBuilder id(Long id) {
            vehicle.setId(id);
            return this;
        }

        public VehicleMockBuilder price(BigDecimal price) {
            vehicle.setPrice(price);
            return this;
        }

        public VehicleMockBuilder status(VehicleStatusEnum status) {
            vehicle.setStatus(status);
            return this;
        }

        @Override
        public Vehicle build() {
            return vehicle;
        }
    }


    public static class VehicleRentalMockBuilder implements EntityMockBuild<VehicleRental> {
        VehicleRental vehicleRental;

        private VehicleRentalMockBuilder() {
            this.vehicleRental = new VehicleRental();
        }

        public VehicleRentalMockBuilder id(Long id) {
            vehicleRental.setId(id);
            return this;
        }

        public VehicleRentalMockBuilder customer(Customer customer) {
            vehicleRental.setCustomer(customer);
            return this;
        }

        public VehicleRentalMockBuilder vehicle(Vehicle vehicle) {
            vehicleRental.setVehicle(vehicle);
            return this;
        }

        public VehicleRentalMockBuilder status(RentStatusEnum status) {
            vehicleRental.setStatus(status);
            return this;
        }

        @Override
        public VehicleRental build() {
            return vehicleRental;
        }
    }


    public static class BillingAccountMockBuilder implements EntityMockBuild<BillingAccount> {
        BillingAccount billingAccount;

        private BillingAccountMockBuilder() {
            this.billingAccount = new BillingAccount();
        }

        public BillingAccountMockBuilder id(Long id) {
            billingAccount.setId(id);
            return this;
        }

        public BillingAccountMockBuilder balance(BigDecimal balance) {
            billingAccount.setBalance(balance);
            return this;
        }

        public BillingAccountMockBuilder currency(Currency currency) {
            billingAccount.setCurrency(currency);
            return this;
        }

        public BillingAccountMockBuilder customer(Customer customer) {
            billingAccount.setCustomer(customer);
            return this;
        }

        @Override
        public BillingAccount build() {
            return billingAccount;
        }
    }


    public static class CustomerMockBuilder implements EntityMockBuild<Customer> {
        Customer customer;

        private CustomerMockBuilder() {
            this.customer = new Customer();
        }

        public CustomerMockBuilder id(Long id) {
            customer.setId(id);
            return this;
        }

        public CustomerMockBuilder status(CustomerStatusEnum status) {
            customer.setStatus(status);
            return this;
        }

        @Override
        public Customer build() {
            return customer;
        }
    }

    public static class CurrencyMockBuilder implements EntityMockBuild<Currency> {
        Currency currency;

        private CurrencyMockBuilder() {
            this.currency = new Currency();
        }

        public CurrencyMockBuilder id(String id) {
            currency.setId(id);
            return this;
        }

        public CurrencyMockBuilder currencyType(CurrencyType type) {
            currency.setCurrencyType(type);
            return this;
        }

        public CurrencyMockBuilder nominal(Integer nominal) {
            currency.setNominal(nominal);
            return this;
        }

        public CurrencyMockBuilder value(BigDecimal value) {
            currency.setValue(value);
            return this;
        }

        @Override
        public Currency build() {
            return currency;
        }
    }
}
