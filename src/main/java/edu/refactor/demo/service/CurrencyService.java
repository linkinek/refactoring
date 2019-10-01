package edu.refactor.demo.service;

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
}
