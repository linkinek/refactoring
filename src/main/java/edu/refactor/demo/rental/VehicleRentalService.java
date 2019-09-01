package edu.refactor.demo.rental;

import edu.refactor.demo.customer.CustomerDAO;
import edu.refactor.demo.vehicle.VehicleDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

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

    @RequestMapping(value = "/rental/create", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
    public @ResponseBody
    VehicleRental createVehicle(@RequestBody VehicleRental rental) {
        return vehicleRentalDao.save(rental);
    }
}
