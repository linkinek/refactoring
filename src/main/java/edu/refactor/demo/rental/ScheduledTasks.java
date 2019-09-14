package edu.refactor.demo.rental;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;

@Component
public class ScheduledTasks {
    private final VehicleRentalDAO vehicleRentalDao;

    @Autowired
    public ScheduledTasks(VehicleRentalDAO vehicleRentalDao) {
        this.vehicleRentalDao = vehicleRentalDao;
    }

    @Scheduled(fixedRate = 5000)
    public void reportCurrentTime() {
        Iterable<VehicleRental> rentalDaoAll = vehicleRentalDao.findAll();
        for (VehicleRental vehicleRental : rentalDaoAll) {
            if (vehicleRental.status.equals("active")) {
                Instant startRent = vehicleRental.startRent;
                long between = Duration.between(startRent, Instant.now()).getSeconds();
                if ("default".equals(vehicleRental.customer.status)) {
                    if (between > 86400) {
                        vehicleRental.status = ("expired");
                    }
                } else if ("vip".equals(vehicleRental.customer.status)) {
                    if (between > 259200) {
                        vehicleRental.status = ("expired");
                    }
                }
            }
            vehicleRentalDao.saveAll(rentalDaoAll);
        }
    }
}