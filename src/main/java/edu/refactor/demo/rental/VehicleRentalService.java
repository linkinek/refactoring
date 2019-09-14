package edu.refactor.demo.rental;

import edu.refactor.demo.customer.BillingAccount;
import edu.refactor.demo.customer.BillingAccountDAO;
import edu.refactor.demo.customer.Customer;
import edu.refactor.demo.customer.CustomerDAO;
import edu.refactor.demo.vehicle.Vehicle;
import edu.refactor.demo.vehicle.VehicleDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@RestController
public class VehicleRentalService {
    private final CustomerDAO customerDAO;
    private final VehicleRentalDAO vehicleRentalDao;
    private final VehicleDAO vehicleDAO;
    private final BillingAccountDAO billingAccountDAO;

    @Autowired
    public VehicleRentalService(CustomerDAO customerDAO, VehicleRentalDAO vehicleRentalDao, VehicleDAO vehicleDAO, BillingAccountDAO billingAccountDAO) {
        this.customerDAO = customerDAO;
        this.vehicleRentalDao = vehicleRentalDao;
        this.vehicleDAO = vehicleDAO;
        this.billingAccountDAO = billingAccountDAO;
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
            if (vehicleRental.status.equals("active")) {
                vehicleRental.endRent = (Instant.now());
                vehicleRental.status = ("completed");
                List<BillingAccount> billingAccounts = vehicleRental.customer.billingAccounts;
                double value = vehicleRental.vehicle.price;
                for (BillingAccount account : billingAccounts) {
                    if (value <= 0) {
                        break;
                    }
                    double v = account.money - value;
                    if (v >= 0) {
                        value -= v;
                        account.money = v;
                    } else {
                        value -= account.money;
                        account.money = 0;
                    }
                }
                if (value < 0) {
                    throw new IllegalStateException("value<0");
                }
                billingAccountDAO.saveAll(billingAccounts);
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
            if (vehicleRental.status.equals("created") || vehicleRental.status.equals("expired")) {
                vehicleRental.endRent = (Instant.now());
                vehicleRental.status = ("active");
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
            if (vehicleRental.status.equals("active")) {
                vehicleRental.endRent = (Instant.now());
                vehicleRental.status = ("expired");
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
            vehicleRental.status = ("created");
            vehicleRental.customer = (customer.get());
            vehicleRental.vehicle = (vehicle.get());
            vehicleRental.startRent = (Instant.now());
            return vehicleRentalDao.save(vehicleRental);
        }
        return null;
    }

}
