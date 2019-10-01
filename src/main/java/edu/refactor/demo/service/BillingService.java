package edu.refactor.demo.service;


/**
 * Сервис предназначен для операциий со счетами заказчиков
 */
public interface BillingService {

    /**
     * <p>Операция завершения аренды автомобиля.</p>
     * <p> Снятие средств с одного из аккаунтов заказчика и перевод аренды в статус: завершено</p>
     *
     * @param vehicleRentId идентификатор активной аренды автомобиля
     */
    void completeRent(Long vehicleRentId);
}
