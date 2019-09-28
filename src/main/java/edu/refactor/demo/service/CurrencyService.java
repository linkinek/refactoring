package edu.refactor.demo.service;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import edu.refactor.demo.currency.soup.model.ValCurs;
import edu.refactor.demo.dao.CurrencyDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CurrencyService {
    private static final Logger logger = LoggerFactory.getLogger(CurrencyService.class);

    public static final String VAL_CURS_FROM_CBR_URL = "http://www.cbr.ru/scripts/XML_daily_eng.asp";

    private CurrencyDAO currencyDAO;

    @Autowired
    public CurrencyService(CurrencyDAO currencyDAO) {
        this.currencyDAO = currencyDAO;
    }

    public void loadCurrenciesFromCbr(){
        ResponseEntity<String> response = new RestTemplate()
                .getForEntity(VAL_CURS_FROM_CBR_URL, String.class);

        ValCurs valCurs = parseXmlResponse(response);

        if(valCurs == null){
            logger.info("Failed to load currencies data from CBR");
            return;
        }

        currencyDAO.saveAll(valCurs);
    }

    @Nullable
    private ValCurs parseXmlResponse(ResponseEntity<String> response) {
        try {
            return new XmlMapper().readValue(response.getBody(), ValCurs.class);
        }catch (Exception e) {
            logger.debug("Error parsing CBR xml response", e);
        }

        return null;
    }
}
