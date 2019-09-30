package edu.refactor.demo.dao;

import edu.refactor.demo.entity.currency.Currency;
import edu.refactor.demo.entity.currency.soup.model.ValCurs;

import java.util.Optional;

public interface CurrencyDAO {
    void saveAll(ValCurs valCurs);

    Optional<Currency> findById(String id);
}
