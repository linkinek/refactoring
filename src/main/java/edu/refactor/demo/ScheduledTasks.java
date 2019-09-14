package edu.refactor.demo;

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
        Iterable<VehicleRental> vrs = vehicleRentalDao.findAll();
        for (VehicleRental vr : vrs) {
            if (vr.status.equals("active")) {
                Instant i = vr.startRent;
                long j = Duration.between(i, Instant.now()).getSeconds();
                if ("default".equals(vr.customer.status)) {
                    if (j > 86400) {
                        vr.status = ("expired");
                    }
                } else if ("vip".equals(vr.customer.status)) {
                    if (j > 259200) {
                        vr.status = ("expired");
                    }
                }
            }
            vehicleRentalDao.saveAll(vrs);
        }
    }
}