package edu.refactor.demo.dao.impl;

import edu.refactor.demo.entity.currency.Currency;
import edu.refactor.demo.entity.currency.soup.model.ValCurs;
import edu.refactor.demo.entity.currency.soup.model.Valute;
import edu.refactor.demo.dao.CurrencyDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.util.Optional;

@Repository
public class CurrencyDAOImpl implements CurrencyDAO {
    private static final Logger logger = LoggerFactory.getLogger(CurrencyDAOImpl.class);

    @PersistenceContext
    private EntityManager em;

    @Transactional
    @Override
    public void saveAll(@NotNull ValCurs valCurs) {
        logger.info("Create or update currency rows with data from CBR");

        for (Valute valute : valCurs.getValutes()) {
            Currency currency = createCurrency(valute);
            em.merge(currency);
        }
    }

    @Transactional
    @Override
    public Optional<Currency> findById(String id) {
        return Optional.ofNullable(em.find(Currency.class, id));
    }

    private Currency createCurrency(Valute valute) {
        Currency currency = new Currency();

        currency.setId(valute.getId());
        currency.setCurrencyType(valute.getCurrencyType());
        currency.setFullName(valute.getName());
        currency.setNominal(valute.getNominal());
        currency.setValue(valute.getValue());

        return currency;
    }
}
