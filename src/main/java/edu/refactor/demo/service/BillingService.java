package edu.refactor.demo.service;


import edu.refactor.demo.entity.currency.Currency;

import java.math.BigDecimal;

/**
 * Сервис предназначен для операциий со счетами заказчиков
 */
public interface BillingService {

    /**
     * <p>Операция снфтия средств со счета</p>
     *
     * @param money кол-во денег, которые необходимо снять,
     *              деньги должны быть в валюте аккауната {@link CurrencyService#convertRubToCurrency}
     * @param accountId идентификатор счета клиента
     *
     * @return результат операции
     */
    boolean cashWithdrawal(BigDecimal money, Long accountId);
}
