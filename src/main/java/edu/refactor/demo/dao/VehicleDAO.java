package edu.refactor.demo.dao;

import edu.refactor.demo.entity.Vehicle;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleDAO extends GenericCrudRepository<Vehicle> {
}