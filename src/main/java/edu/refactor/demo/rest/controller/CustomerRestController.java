package edu.refactor.demo.rest.controller;

import edu.refactor.demo.dao.CustomerDAO;
import edu.refactor.demo.entity.BillingAccount;
import edu.refactor.demo.entity.Customer;
import edu.refactor.demo.entity.status.CategoryEnum;
import edu.refactor.demo.entity.status.CustomerStatusEnum;
import edu.refactor.demo.exception.CustomerAlreadyExistsException;
import edu.refactor.demo.exception.CustomerNotFoundException;
import edu.refactor.demo.service.CurrencyService;
import edu.refactor.demo.service.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;

@RestController
@RequestMapping(value = "/customer")
public class CustomerRestController {

    private static final Logger logger = LoggerFactory.getLogger(CurrencyService.class);

    private CustomerDAO customerDAO;
    private EntityManagerFactory entityManagerFactory;
    private CustomerService customerService;

    @Autowired
    public CustomerRestController(CustomerDAO customerDAO, EntityManagerFactory entityManagerFactory,
                                  CustomerService customerService) {
        this.customerDAO = customerDAO;
        this.entityManagerFactory = entityManagerFactory;
        this.customerService = customerService;
    }

    @RequestMapping(value = "/customer", method = RequestMethod.GET)
    public @ResponseBody
    List<Customer> getAll() {
        return customerDAO.findAll().stream()
                .filter(e -> !"delete".equals(e.getStatus()))
                .collect(Collectors.toList());
    }

    @RequestMapping(value = "/customer/freeze", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity freeze(@RequestParam(name = "login") String login, @RequestParam(name = "email") String email) {
        try {
            customerService.freeze(login, email);

            return ResponseEntity.ok().build();
        } catch (CustomerNotFoundException e) {
            logger.debug("Customer with login {} and email {} " +
                    "not found for freeze", login, email, e);

            return ResponseEntity.notFound().build();
        }
    }

    @RequestMapping(value = "/customer/delete", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity delete(@RequestParam(name = "login") String login, @RequestParam(name = "email") String email) {
        try {
            customerService.delete(login, email);

            return ResponseEntity.ok().build();
        } catch (CustomerNotFoundException e) {
            logger.debug("Customer with login {} and email {} " +
                    "not found for delete", login, email, e);

            return ResponseEntity.notFound().build();
        }
    }

    @RequestMapping(value = "/customer/active", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity active(@RequestParam(name = "login") String login, @RequestParam(name = "email") String email) {
        try {
            customerService.active(login, email);

            return ResponseEntity.ok().build();
        } catch (CustomerNotFoundException e) {
            logger.debug("Customer with login {} and email {} " +
                    "not found for active", login, email, e);

            return ResponseEntity.notFound().build();
        }
    }

    @RequestMapping(value = "/customer/billingAccount", method = RequestMethod.GET)
    public @ResponseBody
    List<BillingAccount> billingAccount(@RequestParam(name = "login") String login,
                                        @RequestParam(name = "email") String email) {
        return customerService.billingAccount(login, email);
    }

    @RequestMapping(value = "/customer/create", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity create(@RequestParam(name = "login") String login,
                          @RequestParam(name = "email") String email) {

        boolean isExists = customerDAO.existsByLoginAndEmail(login, email);

        if (isExists) {
            throw new CustomerAlreadyExistsException(format("Customer already exists " +
                    "with login %s and email %s", login, email));
        }

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction tx = entityManager.getTransaction();
        tx.begin();

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
        entityManager.merge(billingAccount);

        tx.commit();

        return ResponseEntity.ok().build();
    }
}
