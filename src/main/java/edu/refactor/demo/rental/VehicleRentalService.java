package edu.refactor.demo.rental;

import edu.refactor.demo.customer.Customer;
import edu.refactor.demo.customer.CustomerDAO;
import edu.refactor.demo.vehicle.Vehicle;
import edu.refactor.demo.vehicle.VehicleDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Optional;

@RestController
public class VehicleRentalService {
    private final CustomerDAO customerDAO;
    private final VehicleRentalDAO vehicleRentalDao;
    private final VehicleDAO vehicleDAO;

    @Autowired
    public VehicleRentalService(CustomerDAO customerDAO, VehicleRentalDAO vehicleRentalDao, VehicleDAO vehicleDAO) {
        this.customerDAO = customerDAO;
        this.vehicleRentalDao = vehicleRentalDao;
        this.vehicleDAO = vehicleDAO;
    }

    @RequestMapping(value = "/rental", method = RequestMethod.GET)
    public @ResponseBody
    Iterable<VehicleRental> all() {
        return vehicleRentalDao.findAll();
    }

    @RequestMapping(value = "/rental/complete", method = RequestMethod.POST)
    public @ResponseBody
    VehicleRental completeVehicle(@RequestParam(name = "rental") Long rentalId) {
        Optional<VehicleRental> rental = vehicleRentalDao.findById(rentalId);
        if (rental.isPresent()) {
            VehicleRental vehicleRental = rental.get();
            if (vehicleRental.getStatus().equals("active")) {
                vehicleRental.setEndRent(Instant.now());
                vehicleRental.setStatus("completed");
                return vehicleRentalDao.save(vehicleRental);
            }
        }
        return null;
    }

    @RequestMapping(value = "/rental/active", method = RequestMethod.POST)
    public @ResponseBody
    VehicleRental activeVehicle(@RequestParam(name = "rental") Long rentalId) {
        Optional<VehicleRental> rental = vehicleRentalDao.findById(rentalId);
        if (rental.isPresent()) {
            VehicleRental vehicleRental = rental.get();
            if (vehicleRental.getStatus().equals("created")) {
                vehicleRental.setEndRent(Instant.now());
                vehicleRental.setStatus("active");
                return vehicleRentalDao.save(vehicleRental);
            }
        }
        return null;
    }

    @RequestMapping(value = "/rental/expired", method = RequestMethod.POST)
    public @ResponseBody
    VehicleRental expiredVehicle(@RequestParam(name = "rental") Long rentalId) {
        Optional<VehicleRental> rental = vehicleRentalDao.findById(rentalId);
        if (rental.isPresent()) {
            VehicleRental vehicleRental = rental.get();
            if (vehicleRental.getStatus().equals("active")) {
                vehicleRental.setEndRent(Instant.now());
                vehicleRental.setStatus("expired");
                return vehicleRentalDao.save(vehicleRental);
            }
        }
        return null;
    }

    @RequestMapping(value = "/rental/create", method = RequestMethod.POST)
    public @ResponseBody
    VehicleRental createVehicle(@RequestParam(name = "vehicle") Long vehicleId, @RequestParam(name = "customer") Long customerId) {
        Optional<Customer> customer = customerDAO.findById(customerId);
        Optional<Vehicle> vehicle = vehicleDAO.findById(vehicleId);
        if (customer.isPresent() && vehicle.isPresent()) {
            VehicleRental vehicleRental = new VehicleRental();
            vehicleRental.setStatus("created");
            vehicleRental.setCustomer(customer.get());
            vehicleRental.setVehicle(vehicle.get());
            vehicleRental.setStartRent(Instant.now());
            return vehicleRentalDao.save(vehicleRental);
        }
        return null;
    }
}
