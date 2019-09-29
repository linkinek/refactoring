package edu.refactor.demo.shedule;

import edu.refactor.demo.service.VehicleRentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {
    private static final Logger logger = LoggerFactory.getLogger(CbrScheduled.class);
    public static final String UPDATE_CURRENCY_FROM_CBR_CRON = "0 0/30 * * * ?";

    protected VehicleRentService vehicleRentService;

    @Autowired
    public ScheduledTasks(VehicleRentService vehicleRentService) {
        this.vehicleRentService = vehicleRentService;
    }

    @Scheduled(cron = UPDATE_CURRENCY_FROM_CBR_CRON)
    public void reportCurrentTime() {
        logger.info("Rental vehicle status updates started");

        vehicleRentService.updateRentalStatus();
    }
}