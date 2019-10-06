package edu.refactor.demo.service.impl;

import edu.refactor.demo.dao.CustomerDAO;
import edu.refactor.demo.dao.VehicleDAO;
import edu.refactor.demo.dao.VehicleRentalDAO;
import edu.refactor.demo.entity.Customer;
import edu.refactor.demo.entity.Vehicle;
import edu.refactor.demo.entity.VehicleRental;
import edu.refactor.demo.entity.status.CustomerStatusEnum;
import edu.refactor.demo.entity.status.RentStatusEnum;
import edu.refactor.demo.exception.CustomerNotFoundException;
import edu.refactor.demo.exception.VehicleAlreadyTakenException;
import edu.refactor.demo.exception.VehicleRentalNotFoundException;
import edu.refactor.demo.rest.dto.request.RequestVehicleRent;
import edu.refactor.demo.service.VehicleRentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static edu.refactor.demo.entity.status.RentStatusEnum.CREATED;
import static java.util.concurrent.TimeUnit.SECONDS;

@Service
public class VehicleRentServiceImpl implements VehicleRentService {
    private Logger logger = LoggerFactory.getLogger(VehicleRentServiceImpl.class);

    private CustomerDAO customerDAO;

    private VehicleRentalDAO vehicleRentalDAO;

    private VehicleDAO vehicleDAO;

    @Inject
    public VehicleRentServiceImpl(VehicleDAO vehicleDAO, VehicleRentalDAO vehicleRentalDAO,
                                  CustomerDAO customerDAO) {
        this.vehicleDAO = vehicleDAO;
        this.vehicleRentalDAO = vehicleRentalDAO;
        this.customerDAO = customerDAO;
    }

    @Override
    public VehicleRental createVehicleRental(RequestVehicleRent requestRent) {
        Long customerId = requestRent.getCustomerId();
        Long vehicleId = requestRent.getVehicleId();

        Optional<Customer> customerOpt = customerDAO.findById(customerId);

        if (!customerOpt.isPresent()) {
            throw new CustomerNotFoundException(
                    String.format("Customer[%d] not found", customerId));
        }

        Vehicle vehicleOpt = vehicleDAO.findByIdNN(vehicleId);

        Optional<VehicleRental> activeRentOpt = vehicleRentalDAO
                .findActiveRent(vehicleId);

        if (activeRentOpt.isPresent()) {
            throw new VehicleAlreadyTakenException(
                    String.format("Vehicle[%d] is already taken", vehicleId));
        }

        VehicleRental rental = new VehicleRental();

        rental.setStatus(CREATED);
        rental.setCustomer(customerOpt.get());
        rental.setVehicle(vehicleOpt);
        rental.setStartDate(requestRent.getStartDate().toInstant());

        rental = vehicleRentalDAO.save(rental);

        return rental;
    }

    @Override
    public void updateRentalStatus() {
        List<VehicleRental> vehicleRentals = vehicleRentalDAO
                .findVehicleRentalByStatus(RentStatusEnum.ACTIVE);
        for (VehicleRental vehicleRental : vehicleRentals) {
            Instant i = vehicleRental.getStartDate();
            long j = Duration.between(i, Instant.now()).getSeconds();

            CustomerStatusEnum customerStatus = vehicleRental.getCustomer().getStatus();

            if (j > customerStatus.getTimeOut()) {
                vehicleRental.setStatus(RentStatusEnum.EXPIRED);
                vehicleRentalDAO.save(vehicleRental);
            }
        }
    }

    @Override
    public BigDecimal calculateRentCost(Long id) {
        Optional<VehicleRental> rentalOpt = vehicleRentalDAO.findById(id);

        if (!rentalOpt.isPresent()) {
            throw new VehicleRentalNotFoundException();
        }

        VehicleRental rental = rentalOpt.get();
        Vehicle vehicle = rental.getVehicle();
        Customer customer = rental.getCustomer();

        logger.info("Try to calculate cost rent for customer[{}], vehicle[{}]",
                customer.getId(), vehicle.getId());

        long rentalDays = SECONDS.toDays(customer.getStatus().getTimeOut());

        return vehicle.getPrice()
                .multiply(new BigDecimal(rentalDays));
    }
}
