package edu.refactor.demo.service;

import edu.refactor.demo.dao.BillingAccountDAO;
import edu.refactor.demo.dao.VehicleRentalDAO;
import edu.refactor.demo.entity.BillingAccount;
import edu.refactor.demo.entity.Customer;
import edu.refactor.demo.entity.Vehicle;
import edu.refactor.demo.entity.VehicleRental;
import edu.refactor.demo.entity.currency.Currency;
import edu.refactor.demo.entity.status.CustomerStatusEnum;
import edu.refactor.demo.entity.status.RentStatusEnum;
import edu.refactor.demo.exception.CompleteRentException;
import edu.refactor.demo.exception.VehicleRentalNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static java.math.BigDecimal.ROUND_HALF_UP;
import static java.util.concurrent.TimeUnit.SECONDS;

@Service
public class BillingService {
    private static final Logger logger = LoggerFactory.getLogger(BillingService.class);

    private VehicleRentalDAO vehicleRentalDAO;

    private BillingAccountDAO billingAccountDAO;

    @Autowired
    public BillingService(VehicleRentalDAO vehicleRentalDAO, BillingAccountDAO billingAccountDAO) {
        this.vehicleRentalDAO = vehicleRentalDAO;
        this.billingAccountDAO = billingAccountDAO;
    }

    public void completeRent(Long vehicleRentId) {
        Optional<VehicleRental> vehicleRentalOpt = vehicleRentalDAO.findActiveById(vehicleRentId);

        if (!vehicleRentalOpt.isPresent()) {
            throw new VehicleRentalNotFoundException(
                    String.format("Vehicle rental [%d] not found", vehicleRentId));
        }

        VehicleRental rental = vehicleRentalOpt.get();
        Customer customer = rental.getCustomer();

        BigDecimal rentCost = calculateCostRent(rental);

        List<BillingAccount> accounts = billingAccountDAO.findByCustomerId(customer.getId());

        for (BillingAccount account : accounts) {
            BigDecimal balanceRub = convertAccountRub(account);

            if (isAccountPayable(balanceRub, rentCost)) {
                payRent(rental, account, balanceRub);
                return;
            }
        }

        throw new CompleteRentException(
                String.format("Customer[%d] doesn't have payable account", customer.getId()));
    }

    private BigDecimal calculateCostRent(VehicleRental rental) {
        logger.info("Try to calculate cost rent for customer");

        Customer customer = rental.getCustomer();
        CustomerStatusEnum status = customer.getStatus();

        long rentalDays = SECONDS.toDays(status.getTimeOut());
        Vehicle vehicle = rental.getVehicle();

        return vehicle.getPrice().multiply(new BigDecimal(rentalDays));
    }

    private BigDecimal convertAccountRub(BillingAccount account) {
        logger.info("Try to convert account balance to RUB");

        Currency currency = account.getCurrency();

        BigDecimal value = currency.getValue();

        BigDecimal currentRubRate = value.divide(
                new BigDecimal(currency.getNominal()), 4, ROUND_HALF_UP);

        return currentRubRate.multiply(account.getBalance());
    }

    private boolean isAccountPayable(BigDecimal balance, BigDecimal rentCost) {
        return balance.compareTo(rentCost) >= 0;
    }

    private void payRent(VehicleRental rental, BillingAccount account, BigDecimal availableBalance) {
        logger.info("Complete rental[{}],", rental.getId());

        account.setBalance(availableBalance);
        billingAccountDAO.save(account);

        rental.setStatus(RentStatusEnum.COMPLETED);
        vehicleRentalDAO.save(rental);
    }
}
