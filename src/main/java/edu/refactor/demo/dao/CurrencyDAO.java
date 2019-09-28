package edu.refactor.demo.dao;

import edu.refactor.demo.currency.soup.model.ValCurs;

public interface CurrencyDAO {
    void saveAll(ValCurs valCurs);
}
