package edu.refactor.demo.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
public class CustomerService {
    private final CustomerDAO customerDAO;

    private EntityManagerFactory entityManagerFactory;

    @Autowired
    public CustomerService(CustomerDAO customerDAO, EntityManagerFactory entityManagerFactory) {
        this.customerDAO = customerDAO;
        this.entityManagerFactory = entityManagerFactory;
    }

    @RequestMapping(value = "/customer", method = RequestMethod.GET)
    public @ResponseBody
    List<Customer> getAll() {
        return StreamSupport.stream(customerDAO.findAll().spliterator(), false).filter(e -> !e.status.equals("delete")).collect(Collectors.toList());
    }

    @RequestMapping(value = "/customer/freeze", method = RequestMethod.POST)
    public @ResponseBody
    Customer freeze(@RequestParam(name = "login") String login, @RequestParam(name = "email") String email) {
        Iterable<Customer> customers = customerDAO.findAll();
        for (Customer customer : customers) {
            if (customer.login.equals(login) && customer.email.equals(email)) {
                customer.status = ("freeze");
                return customerDAO.save(customer);
            }
        }
        throw new RuntimeException("freeze error");
    }

    @RequestMapping(value = "/customer/delete", method = RequestMethod.POST)
    public @ResponseBody
    Customer delete(@RequestParam(name = "login") String login, @RequestParam(name = "email") String email) {
        Iterable<Customer> customers = customerDAO.findAll();
        for (Customer customer : customers) {
            if (customer.login.equals(login) && customer.email.equals(email)) {
                customer.status = ("delete");
                return customerDAO.save(customer);
            }
        }
        throw new RuntimeException("freeze error");
    }

    @RequestMapping(value = "/customer/active", method = RequestMethod.POST)
    public @ResponseBody
    Customer active(@RequestParam(name = "login") String login, @RequestParam(name = "email") String email) {
        Iterable<Customer> customers = customerDAO.findAll();
        for (Customer customer : customers) {
            if (customer.login.equals(login) && customer.email.equals(email)) {
                customer.status = ("active");
                return customerDAO.save(customer);
            }
        }
        throw new RuntimeException("active error");
    }

    @RequestMapping(value = "/customer/billingAccount", method = RequestMethod.GET)
    public @ResponseBody
    List<BillingAccount> billingAccount(@RequestParam(name = "login") String login, @RequestParam(name = "email") String email) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        return entityManager.createQuery("select e from Customer e JOIN FETCH e.billingAccounts where e.email = :email and e.login = :login  ", Customer.class)
                .setParameter("email", email)
                .setParameter("login", login)
                .getSingleResult()
                .billingAccounts;
    }

    @RequestMapping(value = "/customer/create", method = RequestMethod.POST)
    public @ResponseBody
    Customer create(@RequestParam(name = "login") String login, @RequestParam(name = "email") String email) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        Iterable<Customer> customers = customerDAO.findAll();
        for (Customer customer : customers) {
            if (customer.login.equals(login) || customer.email.equals(email)) {
                throw new RuntimeException("Create customer error");
            }
        }
        Customer customer = new Customer();
        customer.email = (email);
        customer.login = (login);
        customer.category = ("default");
        customer.registration = (Instant.now());
        customer.status = ("active");
        customer = entityManager.merge(customer);
        BillingAccount billingAccount = new BillingAccount();
        billingAccount.customer = customer;
        billingAccount.createdDate = Instant.now();
        billingAccount.isPrimary = true;
        billingAccount.money = 0;
        billingAccount = entityManager.merge(billingAccount);
        transaction.commit();
        return entityManager.find(Customer.class, billingAccount.customer.id);
    }
}
