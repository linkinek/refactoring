package edu.refactor.demo.rental;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {
    protected final VehicleRentalDAO vehicleRentalDao;

    public ScheduledTasks(VehicleRentalDAO vehicleRentalDao) {
        this.vehicleRentalDao = vehicleRentalDao;
    }

    @Scheduled(fixedRate = 5000)
    public void reportCurrentTime() {
        Iterable<VehicleRental> rentalDaoAll = vehicleRentalDao.findAll();
        rentalDaoAll.forEach(e -> {
            if (e.getStatus().equals("active")) {
                switch (e.getCustomer().getStatus()) {
                    case "default": {
                    }
                    break;
                    case "vip": {

                    }
                    break;
                    default: {

                    }
                }
            }
        });
    }
}