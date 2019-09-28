package edu.refactor.demo;

import edu.refactor.demo.dao.CustomerDAO;
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
        Optional<VehicleRental> ro = vehicleRentalDao.findById(rentalId);
        if (ro.isPresent()) {
            VehicleRental vr = ro.get();
            if (vr.status.equals("active")) {
                vr.endRent = (Instant.now());
                vr.status = ("completed");
                List<BillingAccount> bs = vr.customer.billingAccounts;
                double value = vr.vehicle.price;
                for (BillingAccount ba : bs) {
                    double v = ba.money - value;if (v >= 0) { value -= v;ba.money = v; } else { value -= ba.money;ba.money = 0; }
                }
                if (value < 0) {
                    throw new IllegalStateException("value<0");
                }
                billingAccountDAO.saveAll(bs);
                return vehicleRentalDao.save(vr);
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
