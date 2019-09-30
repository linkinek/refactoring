package edu.refactor.demo.dao;

import edu.refactor.demo.entity.BillingAccount;
import edu.refactor.demo.entity.Customer;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerDAO {

    List<Customer> findAll();

    long count();

    Optional<Customer> findById(Long id);

    Customer save(Customer customer);

    Optional<Customer> findByLoginAndEmail(String login, String email);

    void delete(Customer customer);

    List<BillingAccount> retrieveBillingAccounts(String login, String email);

    boolean existsByLoginAndEmail(String login, String email);
}
