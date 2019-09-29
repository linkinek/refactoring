package edu.refactor.demo.service;

import edu.refactor.demo.entity.Vehicle;
import edu.refactor.demo.dao.VehicleDAO;
import edu.refactor.demo.entity.status.VehicleStatusEnum;
import edu.refactor.demo.exception.StatusNotFoundException;
import edu.refactor.demo.exception.VehicleNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
public class VehicleService {
    private VehicleDAO vehicleDAO;

    @Autowired
    public VehicleService(VehicleDAO vehicleDAO) {
        this.vehicleDAO = vehicleDAO;
    }

    @RequestMapping(value = "/vehicle", method = RequestMethod.GET)
    public List<Vehicle> all() {
        return vehicleDAO.findAll();
    }

    @RequestMapping(value = "/vehicle/status/update", method = RequestMethod.POST)
    public void statusUpdate(@RequestParam(name = "serialNumber") String serialNumber,
                                @RequestParam(name = "status") String nextStatus) {
        VehicleStatusEnum nextStatusEnum = VehicleStatusEnum.fromId(nextStatus);

        if(nextStatusEnum == null){
            throw new StatusNotFoundException();
        }

        Vehicle vehicle = vehicleDAO.findBySerialNumber(serialNumber);
        if(vehicle == null){
            throw new VehicleNotFoundException("Vehicle not found by serial number");
        }

        vehicleDAO.updateStatus(vehicle.getId().toString(), nextStatusEnum);
    }

    @RequestMapping(value = "/vehicle/create", method = RequestMethod.POST)
    public void createVehicle(@RequestParam(name = "title") String title,
                                 @RequestParam(name = "price") BigDecimal price,
                                 @RequestParam(name = "type") String type,
                                 @RequestParam(name = "serialNumber") String serialNumber) {
        Vehicle v = new Vehicle();
        v.setStatus(VehicleStatusEnum.OPEN);
        v.setPrice(price);
        v.setTitle(title);
        v.setType(type);
        v.setSerialNumber(serialNumber);

        vehicleDAO.save(v);
    }
}
