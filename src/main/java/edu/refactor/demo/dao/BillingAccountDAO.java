package edu.refactor.demo.dao;

import edu.refactor.demo.entity.BillingAccount;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillingAccountDAO extends GenericCrudRepository<BillingAccount> {

    List<BillingAccount> findByCustomerId(Long customerId);
}