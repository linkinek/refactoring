package edu.refactor.demo.dao;

import edu.refactor.demo.entity.currency.Currency;
import edu.refactor.demo.entity.currency.soup.model.ValCurs;

public interface CurrencyDAO {
    void saveAll(ValCurs valCurs);

    Currency findById(String id);
}
