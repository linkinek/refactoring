package edu.refactor.demo.service;

import edu.refactor.demo.entity.VehicleRental;
import edu.refactor.demo.rest.dto.request.RequestVehicleRent;

import java.math.BigDecimal;

public interface VehicleRentService {
    VehicleRental createVehicleRental(RequestVehicleRent requestRent);

    void updateRentalStatus();

    /**
     * Операция вычисления стоимости аренды автомобиля
     *
     * @param id идентификатор аренды автомобиля
     *
     * @return стоимость аренды
     */
    BigDecimal calculateRentCost(Long id);
}
