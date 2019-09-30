package edu.refactor.demo.dao.impl;

import edu.refactor.demo.dao.CustomerDAO;
import edu.refactor.demo.entity.BillingAccount;
import edu.refactor.demo.entity.Customer;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public class CustomerDAOImpl implements CustomerDAO {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    @Override
    public List<Customer> findAll() {
        return em.createQuery("select e from Customer e", Customer.class)
                .getResultList();
    }

    @Transactional
    @Override
    public long count() {
        return em.createQuery("select count(e) from Customer e", Long.class)
                .getFirstResult();
    }

    @Transactional
    @Override
    public Optional<Customer> findById(Long id) {
        return Optional.of(em.createQuery("select e from Customer e " +
                "where e.id = :id", Customer.class)
                .setParameter("id", id)
                .getSingleResult());
    }

    @Transactional
    @Override
    public Customer save(Customer customer) {
        return em.merge(customer);
    }

    @Transactional
    @Override
    public Optional<Customer> findByLoginAndEmail(String login, String email) {
        return Optional.of(em.createQuery("select e from Customer e " +
                "where e.login = :login " +
                "and e.email = :email", Customer.class)
                .setParameter("login", login)
                .setParameter("email", email)
                .getSingleResult());
    }

    @Transactional
    @Override
    public void delete(Customer customer) {
        em.remove(customer);
    }

    @Transactional
    @Override
    public List<BillingAccount> retrieveBillingAccounts(String login, String email) {
        return em.createQuery("select e.billingAccounts from Customer e " +
                "where e.email = :email and e.login = :login", BillingAccount.class)
                .setParameter("email", email)
                .setParameter("login", login)
                .getResultList();
    }

    @Transactional
    @Override
    public boolean existsByLoginAndEmail(String login, String email) {
        int countUsers = em.createQuery("select count(e) from Customer e " +
                "where e.email = :email and e.login = :login", Long.class)
                .setParameter("email", email)
                .setParameter("login", login)
                .getFirstResult();

        return countUsers > 0;
    }
}
