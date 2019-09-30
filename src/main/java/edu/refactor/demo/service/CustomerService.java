package edu.refactor.demo.service;

import edu.refactor.demo.dao.CustomerDAO;
import edu.refactor.demo.entity.BillingAccount;
import edu.refactor.demo.entity.Customer;
import edu.refactor.demo.entity.status.CategoryEnum;
import edu.refactor.demo.entity.status.CustomerStatusEnum;
import edu.refactor.demo.exception.CustomerNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {
    private CustomerDAO customerDAO;

    private EntityManagerFactory entityManagerFactory;

    //TODO please add logs for all methods...
    @Autowired
    public CustomerService(CustomerDAO customerDAO, EntityManagerFactory entityManagerFactory) {
        this.customerDAO = customerDAO;
        this.entityManagerFactory = entityManagerFactory;
    }

    public void freeze(String login, String email) {
        Optional<Customer> customerOptional = customerDAO.findByLoginAndEmail(login, email);

        if (!customerOptional.isPresent()) {
            throw new CustomerNotFoundException();
        }

        Customer customer = customerOptional.get();
        customer.setStatus(CustomerStatusEnum.FREEZE);

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
        customer.setStatus(CustomerStatusEnum.ACTIVE);

        customerDAO.save(customer);
    }

    public List<BillingAccount> billingAccount(String login, String email) {
        return customerDAO.retrieveBillingAccounts(login, email);
    }

    @RequestMapping(value = "/customer/create", method = RequestMethod.POST)
    public @ResponseBody
    Customer create(@RequestParam(name = "login") String login, @RequestParam(name = "email") String email) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction tx = entityManager.getTransaction();
        tx.begin();
        Iterable<Customer> customers = customerDAO.findAll();
        for (Customer customer : customers) {
            if (customer.getLogin().equals(login) || customer.getEmail().equals(email)) {
                throw new RuntimeException("Create customer error");
            }
        }
        Customer customer = new Customer();

        customer.setEmail(email);
        customer.setLogin(login);
        customer.setCategory(CategoryEnum.DEFAULT);
        customer.setRegistration(Instant.now());
        customer.setStatus(CustomerStatusEnum.ACTIVE);
        customer = entityManager.merge(customer);

        BillingAccount billingAccount = new BillingAccount();
        billingAccount.setCustomer(customer);
        billingAccount.setCreationDate(Instant.now());
        billingAccount = entityManager.merge(billingAccount);

        tx.commit();
        return entityManager.find(Customer.class, billingAccount.getCustomer().getId());
    }
}
