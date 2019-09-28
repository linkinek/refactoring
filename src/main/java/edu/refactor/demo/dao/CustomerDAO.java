package edu.refactor.demo.dao;

import edu.refactor.demo.Customer;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerDAO extends GenericCrudRepository<Customer> {
}