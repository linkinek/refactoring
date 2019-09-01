package edu.refactor.demo;

import edu.refactor.demo.customer.Customer;
import edu.refactor.demo.customer.CustomerDAO;
import edu.refactor.demo.rental.VehicleRental;
import edu.refactor.demo.rental.VehicleRentalDAO;
import edu.refactor.demo.vehicle.Vehicle;
import edu.refactor.demo.vehicle.VehicleDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.Instant;
import java.util.UUID;

@SpringBootApplication
public class DemoApplication implements CommandLineRunner {
    @Autowired
    private CustomerDAO customerDAO;
    @Autowired
    private VehicleRentalDAO vehicleRentalDAO;
    @Autowired
    private VehicleDAO vehicleDAO;

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Override
    public void run(String... args) {
        Vehicle vehicle = new Vehicle();
        vehicle.setPrice(22500);
        vehicle.setSerialNumber(UUID.randomUUID().toString());
        vehicle.setType("bicycle");
        vehicle.setStatus("ready");
        vehicle = vehicleDAO.save(vehicle);
        Customer customer = new Customer();
        customer.email = "test@yandex.ru";
        customer.login = UUID.randomUUID().toString();
        customer.registration = Instant.now();
        customer.status = "basic";
        customer = customerDAO.save(customer);

        VehicleRental vehicleRental = new VehicleRental();
        vehicleRental.setStartRent(Instant.now());
        vehicleRental.setCustomer(customer);
        vehicleRental.setVehicle(vehicle);
        vehicleRentalDAO.save(vehicleRental);
    }
}