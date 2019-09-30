package edu.refactor.demo.service;

import edu.refactor.demo.dao.CustomerDAO;
import edu.refactor.demo.entity.BillingAccount;
import edu.refactor.demo.entity.Customer;
import edu.refactor.demo.entity.status.CategoryEnum;
import edu.refactor.demo.entity.status.CustomerStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
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

@RestController
public class CustomerService {
    private CustomerDAO customerDAO;

    private EntityManagerFactory entityManagerFactory;

    @Autowired
    public CustomerService(CustomerDAO customerDAO, EntityManagerFactory entityManagerFactory) {
        this.customerDAO = customerDAO;
        this.entityManagerFactory = entityManagerFactory;
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
    Customer freeze(@RequestParam(name = "login") String login, @RequestParam(name = "email") String email) {
        Iterable<Customer> cs = customerDAO.findAll();
        for (Customer c : cs) {
            if (c.getLogin().equals(login) && c.getEmail().equals(email)) {
                c.setStatus(CustomerStatusEnum.FREEZE);
                return customerDAO.save(c);
            }
        }
        throw new RuntimeException("freeze error");
    }

    @RequestMapping(value = "/customer/delete", method = RequestMethod.POST)
    public @ResponseBody
    Customer delete(@RequestParam(name = "login") String login, @RequestParam(name = "email") String email) {
        Iterable<Customer> customers = customerDAO.findAll();
        for (Customer customer : customers) {
            if (customer.getLogin().equals(login) && customer.getEmail().equals(email)) {
                customer.setStatus(CustomerStatusEnum.DEFAULT);
                return customerDAO.save(customer);
            }
        }
        throw new RuntimeException("freeze error");
    }

    @RequestMapping(value = "/customer/active", method = RequestMethod.POST)
    public @ResponseBody
    Customer active(@RequestParam(name = "login") String login, @RequestParam(name = "email") String email) {
        Iterable<Customer> cs = customerDAO.findAll();
        for (Customer c : cs) {
            if (c.getLogin().equals(login) && c.getEmail().equals(email)) {
                c.setStatus(CustomerStatusEnum.ACTIVE);
                return customerDAO.save(c);
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
                .getBillingAccounts();
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
