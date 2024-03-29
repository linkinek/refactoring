package edu.refactor.demo.shedule;

import edu.refactor.demo.service.CurrencyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component
public class CbrScheduled {
    private static final Logger logger = LoggerFactory.getLogger(CbrScheduled.class);
    public static final String UPDATE_CURRENCY_FROM_CBR_CRON = "0/5 * * * * ?";

    private CurrencyService currencyService;

    @Inject
    public CbrScheduled(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @Scheduled(cron = UPDATE_CURRENCY_FROM_CBR_CRON)
    public void loadCurrenciesDataFromCbr() {
        logger.info("Try to load currencies data from CBR");

        currencyService.syncCurrenciesFromCbr();
    }
}
