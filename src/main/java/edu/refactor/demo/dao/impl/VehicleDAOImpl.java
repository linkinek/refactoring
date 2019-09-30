package edu.refactor.demo.dao.impl;

import edu.refactor.demo.dao.VehicleDAO;
import edu.refactor.demo.entity.Vehicle;
import edu.refactor.demo.entity.status.VehicleStatusEnum;
import edu.refactor.demo.exception.VehicleNotFoundException;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.util.List;

@Repository
public class VehicleDAOImpl implements VehicleDAO {

    @PersistenceContext
    private EntityManager em;


    @Transactional
    @Override
    public Vehicle findBySerialNumber(@NotNull String serialNumber) {
        return em.createQuery("select v from Vehicle v where v.serialNumber = :serialNumber", Vehicle.class)
                .setParameter("serialNumber", serialNumber)
                .getSingleResult();
    }

    @Nullable
    @Override
    public Vehicle findById(String id) {
        Vehicle vehicle;
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        vehicle = em.createQuery("select v from Vehicle v where v.id = :id", Vehicle.class)
                .setParameter("id", id)
                .getSingleResult();
        tx.commit();

        return vehicle;
    }

    @Override
    public Vehicle findByIdNN(Long id) {
        return findByIdNN(id.toString());
    }

    @Override
    public Vehicle findByIdNN(String id) {
        Vehicle vehicle = findById(id);
        if (vehicle == null) {
            throw new VehicleNotFoundException(
                    String.format("Vehicle[%d] not found", id));
        }
        return vehicle;
    }

    @Transactional
    @Override
    public List<Vehicle> findAll() {
        return em.createQuery("select v from Vehicle v", Vehicle.class)
                .getResultList();
    }

    @Transactional
    @Override
    public void updateStatus(String vehicleId, VehicleStatusEnum nextStatus) {
        Vehicle vehicle = findByIdNN(vehicleId);

        VehicleStatusEnum status = vehicle.getStatus();
        if (!status.hasCurrentStatuses(nextStatus.getId())) {
            throw new VehicleNotFoundException("The following status is not in the range");
        }

        vehicle.setStatus(nextStatus);
        em.merge(vehicle);
    }

    @Transactional
    @Override
    public void save(Vehicle vehicle) {
        em.merge(vehicle);
    }
}
