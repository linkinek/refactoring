package edu.refactor.demo.dao;

import edu.refactor.demo.entity.VehicleRental;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleRentalDAO extends GenericCrudRepository<VehicleRental> {
}
