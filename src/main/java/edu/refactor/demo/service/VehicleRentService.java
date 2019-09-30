package edu.refactor.demo.service;

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
import edu.refactor.demo.rest.dto.request.RequestVehicleRent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class VehicleRentService {

    private CustomerDAO customerDAO;

    private VehicleRentalDAO vehicleRentalDAO;

    private VehicleDAO vehicleDAO;

    @Autowired
    public VehicleRentService(VehicleDAO vehicleDAO, VehicleRentalDAO vehicleRentalDAO,
                              CustomerDAO customerDAO) {
        this.vehicleDAO = vehicleDAO;
        this.vehicleRentalDAO = vehicleRentalDAO;
        this.customerDAO = customerDAO;
    }

    public VehicleRental createVehicleRental(RequestVehicleRent requestRent){
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

        if(activeRentOpt.isPresent()){
            throw new VehicleAlreadyTakenException(
                    String.format("Vehicle[%d] is already taken", vehicleId));
        }

        //TODO check available rental

        VehicleRental rental = new VehicleRental();

        rental.setStatus(RentStatusEnum.CREATED);
        rental.setCustomer(customerOpt.get());
        rental.setVehicle(vehicleOpt);
        rental.setStartDate(requestRent.getStartDate().toInstant());

        VehicleRental rentalSaved = vehicleRentalDAO.save(rental);

        return rentalSaved;
    }

    public void updateRentalStatus(){
        List<VehicleRental> vehicleRentals = vehicleRentalDAO
                .findVehicleRentalByStatus(RentStatusEnum.ACTIVE);
        for (VehicleRental vehicleRental : vehicleRentals) {
            Instant i = vehicleRental.getStartDate();
            long j = Duration.between(i, Instant.now()).getSeconds();

            CustomerStatusEnum customerStatus = vehicleRental.getCustomer().getStatus();

            if(j > customerStatus.getTimeOut()){
                vehicleRental.setStatus(RentStatusEnum.EXPIRED);
                vehicleRentalDAO.save(vehicleRental);
            }
        }

    }
}
