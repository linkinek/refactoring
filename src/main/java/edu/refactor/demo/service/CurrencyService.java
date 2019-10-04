package edu.refactor.demo.service;

import edu.refactor.demo.entity.currency.Currency;

import java.math.BigDecimal;

/**
 * Сервис предназанчен для операций с текущими мироваыми валютами
 */
public interface CurrencyService {
    /**
     * URL получения актуальных курсов валют из ЦентраБанка
     */
    String ACTUAL_CURRENCY_FROM_CBR_URL = "http://www.cbr.ru/scripts/XML_daily_eng.asp";

    /**
     * Операция выгрузки актуальных курсов валют в рублях из ЦентраБанка
     */
    void syncCurrenciesFromCbr();

    /**
     * Операция конвертации рублевых денежных средств в валюту
     *
     * @param money Денежные средства в рублях
     * @param currency Валюта в которую необъходимо сконвертировать
     *
     * @return денежные средства сконвертированные в валюту
     */
    BigDecimal convertRubToCurrency(BigDecimal money, Currency currency);
}
