package edu.refactor.demo.service.util;

import edu.refactor.demo.entity.BillingAccount;
import edu.refactor.demo.entity.Customer;
import edu.refactor.demo.entity.Vehicle;
import edu.refactor.demo.entity.VehicleRental;
import edu.refactor.demo.entity.currency.Currency;
import edu.refactor.demo.service.util.EntityMockBuilder.BillingAccountMockBuilder;
import edu.refactor.demo.service.util.EntityMockBuilder.CurrencyMockBuilder;
import edu.refactor.demo.service.util.EntityMockBuilder.CustomerMockBuilder;
import edu.refactor.demo.service.util.EntityMockBuilder.VehicleMockBuilder;
import edu.refactor.demo.service.util.EntityMockBuilder.VehicleRentalMockBuilder;

public abstract class AbstractDefaultServiceTest {

    protected VehicleMockBuilder vehicleBuilder = EntityMockBuilder.mockTo(Vehicle.class);

    protected VehicleRentalMockBuilder rentBuilder = EntityMockBuilder.mockTo(VehicleRental.class);

    protected BillingAccountMockBuilder accountBuilder = EntityMockBuilder.mockTo(BillingAccount.class);

    protected CustomerMockBuilder customerBuilder = EntityMockBuilder.mockTo(Customer.class);

    protected CurrencyMockBuilder currencyBuilder = EntityMockBuilder.mockTo(Currency.class);
}
