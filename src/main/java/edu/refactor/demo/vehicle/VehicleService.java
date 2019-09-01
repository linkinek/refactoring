package edu.refactor.demo.vehicle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class VehicleService {
    private final VehicleDAO vehicleDAO;

    @Autowired
    public VehicleService(VehicleDAO vehicleDAO) {
        this.vehicleDAO = vehicleDAO;
    }

    @RequestMapping(value = "/vehicle", method = RequestMethod.GET)
    public @ResponseBody
    Iterable<Vehicle> all() {
        return vehicleDAO.findAll();
    }

    @RequestMapping(value = "/vehicle/status/update", method = RequestMethod.POST)
    public @ResponseBody
    boolean statusUpdate(@RequestParam(name = "serialNumber") String serialNumber, @RequestParam(name = "status") String nextStatus) {
        if (nextStatus.equals("delete")) {
            for (Vehicle vehicle : vehicleDAO.findAll()) {
                if (vehicle.getSerialNumber().equals(serialNumber)) {
                    if (vehicle.getStatus().equals("open")) {
                        vehicle.setStatus(nextStatus);
                        vehicleDAO.save(vehicle);
                        return true;
                    }
                    return false;
                }
            }
        }
        if (nextStatus.equals("reserved")) {
            for (Vehicle vehicle : vehicleDAO.findAll()) {
                if (vehicle.getSerialNumber().equals(serialNumber)) {
                    if (vehicle.getStatus().equals("open")) {
                        vehicle.setStatus(nextStatus);
                        vehicleDAO.save(vehicle);
                        return true;
                    }
                    return false;
                }
            }
        }
        if (nextStatus.equals("leased")) {
            for (Vehicle vehicle : vehicleDAO.findAll()) {
                if (vehicle.getSerialNumber().equals(serialNumber)) {
                    if (vehicle.getStatus().equals("reserved")) {
                        vehicle.setStatus(nextStatus);
                        vehicleDAO.save(vehicle);
                        return true;
                    }
                    return false;
                }
            }
        }

        if (nextStatus.equals("lost")) {
            for (Vehicle vehicle : vehicleDAO.findAll()) {
                if (vehicle.getSerialNumber().equals(serialNumber)) {
                    if (vehicle.getStatus().equals("leased")) {
                        vehicle.setStatus(nextStatus);
                        vehicleDAO.save(vehicle);
                        return true;
                    }
                    return false;
                }
            }
        }

        if (nextStatus.equals("returned")) {
            for (Vehicle vehicle : vehicleDAO.findAll()) {
                if (vehicle.getSerialNumber().equals(serialNumber)) {
                    if (vehicle.getStatus().equals("leased")) {
                        vehicle.setStatus(nextStatus);
                        vehicleDAO.save(vehicle);
                        return true;
                    }
                    return false;
                }
            }
        }

        if (nextStatus.equals("service")) {
            for (Vehicle vehicle : vehicleDAO.findAll()) {
                if (vehicle.getSerialNumber().equals(serialNumber)) {
                    if (vehicle.getStatus().equals("returned")) {
                        vehicle.setStatus(nextStatus);
                        vehicleDAO.save(vehicle);
                        return true;
                    }
                    return false;
                }
            }
        }


        if (nextStatus.equals("open")) {
            for (Vehicle vehicle : vehicleDAO.findAll()) {
                if (vehicle.getSerialNumber().equals(serialNumber)) {
                    if (vehicle.getStatus().equals("service")) {
                        vehicle.setStatus(nextStatus);
                        vehicleDAO.save(vehicle);
                        return true;
                    }
                    return false;
                }
            }
        }
        if (nextStatus.equals("open")) {
            for (Vehicle vehicle : vehicleDAO.findAll()) {
                if (vehicle.getSerialNumber().equals(serialNumber)) {
                    if (vehicle.getStatus().equals("returned")) {
                        vehicle.setStatus(nextStatus);
                        vehicleDAO.save(vehicle);
                        return true;
                    }
                    return false;
                }
            }
        }
        return false;
    }

    @RequestMapping(value = "/vehicle/create", method = RequestMethod.POST)
    public @ResponseBody
    Vehicle createVehicle(@RequestParam(name = "title") String title,
                          @RequestParam(name = "price") double price,
                          @RequestParam(name = "type") String type,
                          @RequestParam(name = "serialNumber") String serialNumber) {
        Vehicle vehicle = new Vehicle();
        vehicle.status = "open";
        vehicle.price = price;
        vehicle.title = title;
        vehicle.type = type;
        vehicle.setSerialNumber(serialNumber);
        return vehicleDAO.save(vehicle);
    }
}
