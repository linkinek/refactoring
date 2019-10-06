package edu.refactor.demo.rest.controller;

import edu.refactor.demo.dao.VehicleDAO;
import edu.refactor.demo.entity.Vehicle;
import edu.refactor.demo.entity.status.VehicleStatusEnum;
import edu.refactor.demo.exception.StatusNotFoundException;
import edu.refactor.demo.exception.VehicleNotFoundException;
import edu.refactor.demo.rest.dto.request.RequestVehicle;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import javax.validation.Valid;
import java.util.List;

@RestController
public class VehicleRestController {
    private VehicleDAO vehicleDAO;

    @Inject
    public VehicleRestController(VehicleDAO vehicleDAO) {
        this.vehicleDAO = vehicleDAO;
    }

    @RequestMapping(value = "/vehicle", method = RequestMethod.GET)
    public List<Vehicle> getVehicles() {
        return vehicleDAO.findAll();
    }

    @RequestMapping(value = "/vehicle/status/update", method = RequestMethod.POST)
    public ResponseEntity updateVehicleStatus(@RequestParam(name = "serialNumber") String serialNumber,
                                              @RequestParam(name = "status") String status) {
        VehicleStatusEnum nextStatus = VehicleStatusEnum.fromId(status);

        if (nextStatus == null) {
            throw new StatusNotFoundException();
        }

        Vehicle vehicle = vehicleDAO.findBySerialNumber(serialNumber);

        if (vehicle == null) {
            throw new VehicleNotFoundException("Vehicle not found by serial number");
        }

        vehicleDAO.updateStatus(vehicle.getId().toString(), nextStatus);

        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/vehicle/create", method = RequestMethod.POST)
    public ResponseEntity createVehicle(@Valid RequestVehicle request) {
        Vehicle v = new Vehicle();

        v.setPrice(request.getPrice());
        v.setTitle(request.getTitle());
        v.setType(request.getType());
        v.setSerialNumber(request.getSerialNumber());
        v.setStatus(VehicleStatusEnum.OPEN);

        vehicleDAO.save(v);

        return ResponseEntity.ok().build();
    }
}
