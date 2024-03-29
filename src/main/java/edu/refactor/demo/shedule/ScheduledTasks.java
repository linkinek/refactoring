package edu.refactor.demo.shedule;

import edu.refactor.demo.service.VehicleRentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component
public class ScheduledTasks {
    private static final Logger logger = LoggerFactory.getLogger(CbrScheduled.class);

    public static final String UPDATE_CURRENT_RENTAL_STATUS_CRON = "0 0 0 * * *";

    protected VehicleRentService vehicleRentService;

    @Inject
    public ScheduledTasks(VehicleRentService vehicleRentService) {
        this.vehicleRentService = vehicleRentService;
    }

    @Scheduled(cron = UPDATE_CURRENT_RENTAL_STATUS_CRON)
    public void reportCurrentTime() {
        logger.info("Rental vehicle status updates started");

        vehicleRentService.updateRentalStatus();
    }
}