package edu.refactor.demo.service;

import edu.refactor.demo.dao.CustomerDAO;
import edu.refactor.demo.dao.VehicleDAO;
import edu.refactor.demo.dao.VehicleRentalDAO;
import edu.refactor.demo.entity.Customer;
import edu.refactor.demo.entity.Vehicle;
import edu.refactor.demo.entity.VehicleRental;
import edu.refactor.demo.exception.CustomerNotFoundException;
import edu.refactor.demo.exception.VehicleAlreadyTakenException;
import edu.refactor.demo.exception.VehicleNotFoundException;
import edu.refactor.demo.rest.dto.request.RequestVehicleRent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class VehicleRentService {

    private CustomerDAO customerDAO;

    private BillingService billingService;

    private VehicleRentalDAO vehicleRentalDAO;

    private VehicleDAO vehicleDAO;

    @Autowired
    public VehicleRentService(BillingService billingService, VehicleDAO vehicleDAO,
                              VehicleRentalDAO vehicleRentalDAO, CustomerDAO customerDAO) {
        this.billingService = billingService;
        this.vehicleDAO = vehicleDAO;
        this.vehicleRentalDAO = vehicleRentalDAO;
        this.customerDAO = customerDAO;
    }

    public VehicleRental rentVehicle(RequestVehicleRent requestRent){
        Long customerId = requestRent.getCustomerId();
        Long vehicleId = requestRent.getVehicleId();

        Optional<Customer> customerOpt = customerDAO.findById(customerId);

        if (!customerOpt.isPresent()) {
            throw new CustomerNotFoundException(
                    String.format("Customer[%d] not found", customerId));
        }

        Optional<Vehicle> vehicleOpt = vehicleDAO.findById(vehicleId);

        if (!vehicleOpt.isPresent()) {
            throw new VehicleNotFoundException(
                    String.format("Vehicle[%d] not found", vehicleId));
        }

        Date startDate = requestRent.getStartDate();
        Date endDate = requestRent.getEndDate();

        Optional<VehicleRental> activeRentOpt = vehicleRentalDAO
                .findActiveRent(vehicleId, startDate, endDate);

        if(activeRentOpt.isPresent()){
            throw new VehicleAlreadyTakenException(
                    String.format("Vehicle[%d] is already taken", vehicleId));
        }

        //TODO check available rental

        VehicleRental rental = new VehicleRental();

        rental.setStatus("created");
        rental.setCustomer(customerOpt.get());
        rental.setVehicle(vehicleOpt.get());
        rental.setStartDate(requestRent.getStartDate().toInstant());
        rental.setEndDate(requestRent.getEndDate().toInstant());

        VehicleRental rentalSaved = vehicleRentalDAO.save(rental);

        return rentalSaved;
    }
}
