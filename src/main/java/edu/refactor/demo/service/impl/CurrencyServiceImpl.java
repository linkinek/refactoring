package edu.refactor.demo.service.impl;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import edu.refactor.demo.dao.CurrencyDAO;
import edu.refactor.demo.entity.currency.soup.model.ValCurs;
import edu.refactor.demo.service.CurrencyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CurrencyServiceImpl implements CurrencyService {
    private static final Logger logger = LoggerFactory.getLogger(CurrencyServiceImpl.class);

    private CurrencyDAO currencyDAO;

    private XmlMapper xmlMapper;

    @Autowired
    public CurrencyServiceImpl(CurrencyDAO currencyDAO, XmlMapper xmlMapper) {
        this.currencyDAO = currencyDAO;
        this.xmlMapper = xmlMapper;
    }

    @Override
    public void loadCurrenciesFromCbr(){
        ResponseEntity<String> response = new RestTemplate()
                .getForEntity(ACTUAL_CURRENCY_FROM_CBR_URL, String.class);

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
            return xmlMapper.readValue(response.getBody(), ValCurs.class);
        }catch (Exception e) {
            logger.debug("Error parsing CBR xml response", e);
        }

        return null;
    }
}
