package edu.refactor.demo.service;

import edu.refactor.demo.dao.BillingAccountDAO;
import edu.refactor.demo.dao.CustomerDAO;
import edu.refactor.demo.entity.BillingAccount;
import edu.refactor.demo.entity.Customer;
import edu.refactor.demo.exception.CustomerNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static edu.refactor.demo.entity.status.CategoryEnum.DEFAULT;
import static edu.refactor.demo.entity.status.CustomerStatusEnum.ACTIVE;
import static edu.refactor.demo.entity.status.CustomerStatusEnum.FREEZE;

@Service
public class CustomerService {
    private CustomerDAO customerDAO;

    private BillingAccountDAO billingAccountDAO;

    //TODO please add logs for all methods...
    @Autowired
    public CustomerService(CustomerDAO customerDAO, BillingAccountDAO billingAccountDAO) {
        this.customerDAO = customerDAO;
        this.billingAccountDAO = billingAccountDAO;
    }

    public void freeze(String login, String email) {
        Optional<Customer> customerOptional = customerDAO.findByLoginAndEmail(login, email);

        if (!customerOptional.isPresent()) {
            throw new CustomerNotFoundException();
        }

        Customer customer = customerOptional.get();
        customer.setStatus(FREEZE);

        customerDAO.save(customer);
    }

    public void delete(String login, String email) {
        Optional<Customer> customerOptional = customerDAO.findByLoginAndEmail(login, email);

        if (!customerOptional.isPresent()) {
            throw new CustomerNotFoundException();
        }

        Customer customer = customerOptional.get();
        customerDAO.delete(customer);
    }

    public void active(String login, String email) {
        Optional<Customer> customerOptional = customerDAO.findByLoginAndEmail(login, email);

        if (!customerOptional.isPresent()) {
            throw new CustomerNotFoundException();
        }

        Customer customer = customerOptional.get();
        customer.setStatus(ACTIVE);

        customerDAO.save(customer);
    }

    public List<BillingAccount> billingAccount(String login, String email) {
        return customerDAO.retrieveBillingAccounts(login, email);
    }

    public void create(String login, String email) {
        Customer customer = new Customer();

        customer.setEmail(email);
        customer.setLogin(login);
        customer.setCategory(DEFAULT);
        customer.setRegistrationDate(Instant.now());
        customer.setStatus(ACTIVE);

        customer = customerDAO.save(customer);

        BillingAccount billingAccount = new BillingAccount();
        billingAccount.setCustomer(customer);
        billingAccount.setCreationDate(Instant.now());

        billingAccountDAO.save(billingAccount);
    }
}
