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
    private static final Logger logger = LoggerFactory.getLogger(VehicleRentalDAOImpl.class);

    public static final String SELECT_RENT_ALL_QUERY = "select e from VehicleRental e";

    @PersistenceContext
    private EntityManager em;

    @Transactional
    @Override
    public List<VehicleRental> findAll() {
        return em.createQuery(SELECT_RENT_ALL_QUERY, VehicleRental.class).getResultList();
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

    @Override //TODO
    public Optional<VehicleRental> findActiveRent(Long vehicleId, Date startDate, Date endDate) {
        return Optional.empty();
    }

    @Override //TODO
    public Optional<VehicleRental> findActiveById(Long vehicleRentId) {
        return Optional.empty();
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
