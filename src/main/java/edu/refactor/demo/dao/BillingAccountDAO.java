package edu.refactor.demo.dao;

import edu.refactor.demo.entity.BillingAccount;
import org.springframework.stereotype.Repository;

@Repository
public interface BillingAccountDAO extends GenericCrudRepository<BillingAccount> {
}