package edu.refactor.demo.dao;

import edu.refactor.demo.entity.VehicleRental;
import edu.refactor.demo.entity.status.RentStatusEnum;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface VehicleRentalDAO {
    List<VehicleRental> findAll();

    Optional<VehicleRental> findById(Long id);

    VehicleRental save(VehicleRental rental);

    void saveAll(Iterable<VehicleRental> vrs);

    Optional<VehicleRental> findActiveRent(Long vehicleId);

    List<VehicleRental> findVehicleRentalByStatus(RentStatusEnum status);
}
