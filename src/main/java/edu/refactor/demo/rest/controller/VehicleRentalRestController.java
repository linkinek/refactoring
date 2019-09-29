package edu.refactor.demo.rest.controller;

import edu.refactor.demo.entity.BillingAccount;
import edu.refactor.demo.entity.Customer;
import edu.refactor.demo.entity.Vehicle;
import edu.refactor.demo.dao.VehicleDAO;
import edu.refactor.demo.entity.VehicleRental;
import edu.refactor.demo.dao.VehicleRentalDAO;
import edu.refactor.demo.dao.BillingAccountDAO;
import edu.refactor.demo.dao.CustomerDAO;
import edu.refactor.demo.exception.CustomerNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/vehicle-rental")
public class VehicleRentalRestController {
    private static final Logger logger = LoggerFactory.getLogger(VehicleRentalRestController.class);

    private CustomerDAO customerDAO;

    private VehicleRentalDAO vehicleRentalDao;

    private VehicleDAO vehicleDAO;

    private BillingAccountDAO billingAccountDAO;

    @Autowired
    public VehicleRentalRestController(CustomerDAO customerDAO, VehicleRentalDAO vehicleRentalDao,
                                       VehicleDAO vehicleDAO, BillingAccountDAO billingAccountDAO) {
        this.customerDAO = customerDAO;
        this.vehicleRentalDao = vehicleRentalDao;
        this.vehicleDAO = vehicleDAO;
        this.billingAccountDAO = billingAccountDAO;
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public List<VehicleRental> getVehicleRentals() {
        return vehicleRentalDao.findAll();
    }

    @RequestMapping(value = "/rental/complete", method = RequestMethod.POST)
    public VehicleRental completeVehicle(@RequestParam(name = "rental") Long rentalId) {
        Optional<VehicleRental> ro = vehicleRentalDao.findById(rentalId);
        if (ro.isPresent()) {
            VehicleRental vr = ro.get();
            if (vr.getStatus().equals("active")) {
                vr.setEndDate(Instant.now());
                vr.setStatus("completed");
                List<BillingAccount> bs = vr.getCustomer().getBillingAccounts();
                double value = vr.getVehicle().getPrice();
                for (BillingAccount ba : bs) {
//                    double v = ba.getMoney() - value;
//                    if (v >= 0) {
//                        value -= v;
//                        ba.setMoney(v);
//                    } else {
//                        value -= ba.getMoney();
//                        ba.setMoney(0);
//                    }
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
            if (vehicleRental.getStatus().equals("created") || vehicleRental.getStatus().equals("expired")) {
                vehicleRental.setEndDate(Instant.now());
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
                vehicleRental.setEndDate(Instant.now());
                vehicleRental.setStatus("expired");
                return vehicleRentalDao.save(vehicleRental);
            }
        }
        return null;
    }

    @RequestMapping(value = "/request", method = RequestMethod.POST)
    public @ResponseBody
    VehicleRental requestForVehicleRent(@RequestParam(name = "vehicleId") Long vehicleId,
                                        @RequestParam(name = "customerId") Long customerId) {

        Optional<Customer> customerOpt = customerDAO.findById(customerId);

        if(!customerOpt.isPresent()){
            throw new CustomerNotFoundException(
                    String.format("Customer[%s] not found", customerId));
        }


        Optional<Vehicle> vehicle = vehicleDAO.findById(vehicleId);
        if (customerOpt.isPresent() && vehicle.isPresent()) {
            VehicleRental vehicleRental = new VehicleRental();
            vehicleRental.setStatus("created");
            vehicleRental.setCustomer(customerOpt.get());
            vehicleRental.setVehicle(vehicle.get());
            vehicleRental.setStartDate(Instant.now());
            return vehicleRentalDao.save(vehicleRental);
        }
        return null;
    }

}
