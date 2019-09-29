package edu.refactor.demo.shedule;

import edu.refactor.demo.dao.VehicleRentalDAO;
import edu.refactor.demo.entity.VehicleRental;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;

@Component
public class ScheduledTasks {
    private VehicleRentalDAO vehicleRentalDao;

    @Autowired
    public ScheduledTasks(VehicleRentalDAO vehicleRentalDao) {
        this.vehicleRentalDao = vehicleRentalDao;
    }

   // @Scheduled(fixedRate = 1000)
    public void reportCurrentTime() {
        Iterable<VehicleRental> vrs = vehicleRentalDao.findAll();
        for (VehicleRental vr : vrs) {
            String status = vr.getStatus();
            if ("active".equals(status)) {
                Instant i = vr.getStartDate();
                long j = Duration.between(i, Instant.now()).getSeconds();
                if ("default".equals(vr.getCustomer().getStatus())) {
                    if (j > 86400) {
                        vr.setStatus("expired");
                    }
                } else if ("vip".equals(vr.getCustomer().getStatus())) {
                    if (j > 259200) {
                        vr.setStatus("expired");
                    }
                }
            }
            vehicleRentalDao.saveAll(vrs);
        }
    }
}