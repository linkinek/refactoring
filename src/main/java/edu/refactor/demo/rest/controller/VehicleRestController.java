package edu.refactor.demo.rest.controller;

import edu.refactor.demo.dao.VehicleDAO;
import edu.refactor.demo.entity.Vehicle;
import edu.refactor.demo.entity.status.VehicleStatusEnum;
import edu.refactor.demo.exception.StatusNotFoundException;
import edu.refactor.demo.exception.VehicleNotFoundException;
import edu.refactor.demo.rest.dto.request.RequestVehicle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
public class VehicleRestController {
    private VehicleDAO vehicleDAO;

    @Autowired
    public VehicleRestController(VehicleDAO vehicleDAO) {
        this.vehicleDAO = vehicleDAO;
    }

    @RequestMapping(value = "/vehicle", method = RequestMethod.GET)
    public List<Vehicle> getVehicles() {
        return vehicleDAO.findAll();
    }

    @RequestMapping(value = "/vehicle/status/update", method = RequestMethod.POST)
    public void updateVehicleStatus(@RequestParam(name = "serialNumber") String serialNumber,
                                    @RequestParam(name = "status") String nextStatus) {
        VehicleStatusEnum nextStatusEnum = VehicleStatusEnum.fromId(nextStatus);

        if (nextStatusEnum == null) {
            throw new StatusNotFoundException();
        }

        Vehicle vehicle = vehicleDAO.findBySerialNumber(serialNumber);

        if (vehicle == null) {
            throw new VehicleNotFoundException("Vehicle not found by serial number");
        }

        vehicleDAO.updateStatus(vehicle.getId().toString(), nextStatusEnum);
    }

    @RequestMapping(value = "/vehicle/create", method = RequestMethod.POST)
    public void createVehicle(@Valid RequestVehicle request) {
        Vehicle v = new Vehicle();

        v.setPrice(request.getPrice());
        v.setTitle(request.getTitle());
        v.setType(request.getType());
        v.setSerialNumber(request.getSerialNumber());
        v.setStatus(VehicleStatusEnum.OPEN);

        vehicleDAO.save(v);
    }
}
