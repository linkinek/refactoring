package edu.refactor.demo.rest.controller;

import edu.refactor.demo.dao.VehicleRentalDAO;
import edu.refactor.demo.entity.VehicleRental;
import edu.refactor.demo.rest.dto.request.RequestVehicleRent;
import edu.refactor.demo.rest.dto.response.ResponseVehicleRent;
import edu.refactor.demo.service.BillingService;
import edu.refactor.demo.service.VehicleRentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/vehicle-rental")
public class VehicleRentalRestController {
    private VehicleRentalDAO vehicleRentalDao;

    private VehicleRentService vehicleRentService;

    private BillingService billingService;

    @Autowired
    public VehicleRentalRestController(VehicleRentalDAO vehicleRentalDao,
                                       BillingService billingService,
                                       VehicleRentService vehicleRentService) {
        this.vehicleRentalDao = vehicleRentalDao;
        this.vehicleRentService = vehicleRentService;
        this.billingService = billingService;
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public List<VehicleRental> getVehicleRentals() {
        return vehicleRentalDao.findAll();
    }

    @RequestMapping(value = "/rental/complete", method = RequestMethod.POST)
    public void completeVehicle(@RequestParam(name = "rental") Long rentalId) {
        billingService.completeRent(rentalId);
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
    public ResponseVehicleRent requestForVehicleRent(@Valid RequestVehicleRent requestRent) {
        VehicleRental vehicleRental = vehicleRentService.rentVehicle(requestRent);
        return new ResponseVehicleRent(vehicleRental.getId(), vehicleRental.getStatus());
    }

}
