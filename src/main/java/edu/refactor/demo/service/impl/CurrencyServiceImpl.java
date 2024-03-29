package edu.refactor.demo.service.impl;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import edu.refactor.demo.dao.CurrencyDAO;
import edu.refactor.demo.entity.currency.Currency;
import edu.refactor.demo.entity.currency.soup.model.ValCurs;
import edu.refactor.demo.service.CurrencyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.inject.Inject;
import java.math.BigDecimal;

import static java.math.BigDecimal.ROUND_HALF_UP;

@Service
public class CurrencyServiceImpl implements CurrencyService {
    private static final Logger logger = LoggerFactory.getLogger(CurrencyServiceImpl.class);

    public static final int SCALE = 4;

    private CurrencyDAO currencyDAO;

    private XmlMapper xmlMapper;

    @Inject
    public CurrencyServiceImpl(CurrencyDAO currencyDAO, XmlMapper xmlMapper) {
        this.currencyDAO = currencyDAO;
        this.xmlMapper = xmlMapper;
    }

    @Override
    public void syncCurrenciesFromCbr(){
        ResponseEntity<String> response = new RestTemplate()
                .getForEntity(ACTUAL_CURRENCY_FROM_CBR_URL, String.class);

        ValCurs valCurs = parseXmlResponse(response);

        if(valCurs == null){
            logger.info("Failed to load currencies data from CBR");
            return;
        }

        currencyDAO.saveAll(valCurs);
    }

    @Override
    public BigDecimal convertRubToCurrency(BigDecimal money, Currency currency){
        logger.info("Convert money to {}", currency.getCurrencyType());

        BigDecimal value = currency.getValue();

        BigDecimal currentRubRate = value.divide(
                new BigDecimal(currency.getNominal()), SCALE, ROUND_HALF_UP);

        return money.divide(currentRubRate, SCALE, ROUND_HALF_UP);
    }

    @Nullable
    private ValCurs parseXmlResponse(ResponseEntity<String> response) {
        try {
            return xmlMapper.readValue(response.getBody(), ValCurs.class);
        }catch (Exception e) {
            logger.debug("Error parsing CBR xml response", e);
        }

        return null;
    }
}
