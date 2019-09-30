package edu.refactor.demo.dao.impl;

import edu.refactor.demo.dao.VehicleRentalDAO;
import edu.refactor.demo.entity.VehicleRental;
import edu.refactor.demo.entity.status.RentStatusEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public class VehicleRentalDAOImpl implements VehicleRentalDAO {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    @Override
    public List<VehicleRental> findAll() {
        return em.createQuery("select e from VehicleRental e", VehicleRental.class)
                .getResultList();
    }

    @Transactional
    @Override
    public Optional<VehicleRental> findById(Long id) {
        return Optional.ofNullable(em.find(VehicleRental.class, id));
    }

    @Transactional
    @Override
    public VehicleRental save(VehicleRental rental) {
        return em.merge(rental);
    }

    @Transactional
    @Override
    public void saveAll(Iterable<VehicleRental> rentals) {
        rentals.forEach(this::save);
    }

    @Transactional
    @Override
    public Optional<VehicleRental> findActiveRent(Long vehicleId) {
        return Optional.of(em.createQuery("select vr from VehicleRental vr join vr.vehicle v where " +
                "vr.status = :status and v.id = :vehicleId", VehicleRental.class)
                .setParameter("status", RentStatusEnum.ACTIVE)
                .setParameter("vehicleId", vehicleId)
                .getSingleResult());
    }

    @Transactional
    @Override
    public List<VehicleRental> findVehicleRentalByStatus(RentStatusEnum status) {
        return em.createQuery("select vr from VehicleRental vr where " +
                "vr.status = :status order by vr.id", VehicleRental.class)
                .setParameter("status", status.getId())
                .getResultList();
    }
}
