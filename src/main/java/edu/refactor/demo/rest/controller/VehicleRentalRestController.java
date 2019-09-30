package edu.refactor.demo.rest.controller;

import edu.refactor.demo.dao.VehicleRentalDAO;
import edu.refactor.demo.entity.VehicleRental;
import edu.refactor.demo.entity.status.RentStatusEnum;
import edu.refactor.demo.exception.VehicleRentalNotFoundException;
import edu.refactor.demo.rest.dto.request.RequestVehicleRent;
import edu.refactor.demo.rest.dto.response.ResponseVehicleRent;
import edu.refactor.demo.service.BillingService;
import edu.refactor.demo.service.VehicleRentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

import static edu.refactor.demo.entity.status.RentStatusEnum.ACTIVE;
import static edu.refactor.demo.entity.status.RentStatusEnum.CREATED;
import static edu.refactor.demo.entity.status.RentStatusEnum.EXPIRED;

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
    public ResponseEntity completeVehicle(@RequestParam(name = "rental") Long rentalId) {
        billingService.completeRent(rentalId);

        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/rental/active", method = RequestMethod.POST)
    public ResponseEntity activeVehicle(@RequestParam(name = "rental") Long rentalId) {
        Optional<VehicleRental> rental = vehicleRentalDao.findById(rentalId);

        if (!rental.isPresent()) {
            throw new VehicleRentalNotFoundException();
        }

        VehicleRental vehicleRental = rental.get();
        RentStatusEnum rentalStatus = vehicleRental.getStatus();

        if (rentalStatus == CREATED || rentalStatus == EXPIRED) {
            vehicleRental.setStatus(ACTIVE);

            vehicleRentalDao.save(vehicleRental);

            return ResponseEntity.ok().build();
        }

        return ResponseEntity.unprocessableEntity().build();
    }

    @RequestMapping(value = "/rental/expired", method = RequestMethod.POST)
    public ResponseEntity expiredVehicle(@RequestParam(name = "rental") Long rentalId) {
        Optional<VehicleRental> rental = vehicleRentalDao.findById(rentalId);

        if (!rental.isPresent()) {
            throw new VehicleRentalNotFoundException();
        }

        VehicleRental vehicleRental = rental.get();

        if (vehicleRental.getStatus() == ACTIVE) {
            vehicleRental.setStatus(EXPIRED);

            vehicleRentalDao.save(vehicleRental);

            return ResponseEntity.ok().build();
        }

        return ResponseEntity.unprocessableEntity().build();
    }

    @RequestMapping(value = "/request", method = RequestMethod.POST)
    public ResponseEntity<ResponseVehicleRent> requestForVehicleRent(@Valid RequestVehicleRent requestRent) {
        VehicleRental rental = vehicleRentService.createVehicleRental(requestRent);

        return ResponseEntity.ok(new ResponseVehicleRent(rental.getId(), rental.getStatus()));
    }
}
