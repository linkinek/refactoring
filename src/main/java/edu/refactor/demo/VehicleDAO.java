package edu.refactor.demo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleDAO extends CrudRepository<Vehicle, Long> {
}