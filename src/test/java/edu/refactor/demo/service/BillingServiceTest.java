package edu.refactor.demo.service;

import edu.refactor.demo.dao.BillingAccountDAO;
import edu.refactor.demo.dao.VehicleRentalDAO;
import edu.refactor.demo.entity.BillingAccount;
import edu.refactor.demo.entity.Customer;
import edu.refactor.demo.entity.Vehicle;
import edu.refactor.demo.entity.VehicleRental;
import edu.refactor.demo.entity.currency.Currency;
import edu.refactor.demo.service.impl.BillingServiceImpl;
import edu.refactor.demo.service.util.AbstractDefaultServiceTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static edu.refactor.demo.entity.currency.soup.model.CurrencyType.USD;
import static edu.refactor.demo.entity.status.CustomerStatusEnum.VIP;
import static edu.refactor.demo.entity.status.RentStatusEnum.ACTIVE;
import static edu.refactor.demo.entity.status.RentStatusEnum.COMPLETED;
import static edu.refactor.demo.entity.status.VehicleStatusEnum.OPEN;
import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
public class BillingServiceTest extends AbstractDefaultServiceTest {

    @InjectMocks
    private BillingServiceImpl billingService;

    @Mock
    private VehicleRentalDAO vehicleRentalDAO;

    @Mock
    private BillingAccountDAO billingAccountDAO;

    @Before
    public void setUp() throws Exception {
        //configuration test data

        Vehicle vehicle = vehicleBuilder
                .id(1L)
                .price(new BigDecimal(5000))
                .status(OPEN)
                .build();

        Customer customer = customerBuilder
                .id(2L)
                .status(VIP)
                .build();

        Currency currencyUsd = currencyBuilder
                .id("R01235")
                .currencyType(USD)
                .nominal(1)
                .value(new BigDecimal("64.6407"))
                .build();

        BillingAccount account = accountBuilder
                .id(1L)
                .balance(new BigDecimal(500))
                .currency(currencyUsd)
                .customer(customer)
                .build();

        VehicleRental rental = rentBuilder
                .id(3L)
                .status(ACTIVE)
                .customer(customer)
                .vehicle(vehicle)
                .build();

        List<BillingAccount> accounts = Arrays.asList(account);

        customer.setBillingAccounts(accounts);
        customer.setRentals(Arrays.asList(rental));

        vehicle.setRentals(Arrays.asList(rental));

        //mock repository layer

        Mockito.when(vehicleRentalDAO.findActiveRent(rental.getId())).thenReturn(Optional.of(rental));
        Mockito.when(vehicleRentalDAO.findById(rental.getId())).thenReturn(Optional.of(rental));
        Mockito.when(billingAccountDAO.findByCustomerId(customer.getId())).thenReturn(accounts);
        Mockito.when(billingAccountDAO.findById(account.getId())).thenReturn(Optional.of(account));

        Mockito.doReturn(account).when(billingAccountDAO).save(any());
        Mockito.doReturn(rental).when(vehicleRentalDAO).save(any());
    }

    @Test
    public void completeRent() {
        //find rental and account
        Optional<VehicleRental> rentalOpt = vehicleRentalDAO.findById(3L);
        Optional<BillingAccount> accountOpt = billingAccountDAO.findById(1L);

        //check exists
        Assert.assertTrue("Rental not found", rentalOpt.isPresent() );
        Assert.assertTrue("Billing account not found", accountOpt.isPresent());

        VehicleRental rental = rentalOpt.get();
        BillingAccount account = accountOpt.get();

        //calculate cost and complete rental
        billingService.completeRent(rental.getId());

        //check state rental
        Assert.assertEquals("Rental is not completed",rental.getStatus(), COMPLETED);
        //check balance account
        Assert.assertEquals("Incorrect rental calculation",
                new BigDecimal("267.9481"), account.getBalance());
    }
}