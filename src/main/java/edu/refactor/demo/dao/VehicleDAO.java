package edu.refactor.demo.dao;

import edu.refactor.demo.entity.Vehicle;
import edu.refactor.demo.entity.status.VehicleStatusEnum;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleDAO{

    Vehicle findBySerialNumber(String serialNumber);

    Vehicle findById(String id);

    Vehicle findByIdNN(Long id);

    Vehicle findByIdNN(String id);

    List<Vehicle> findAll();

    void updateStatus(String vehicleId, VehicleStatusEnum nextStatus);

    void save(Vehicle vehicle);
}