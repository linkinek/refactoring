package edu.refactor.demo.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
public class CustomerService {
    private final CustomerDAO customerDAO;

    @Autowired
    public CustomerService(CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
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
            if (customer.getLogin().equals(login) && customer.getEmail().equals(email)) {
                customer.setStatus("freeze");
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
            if (customer.getLogin().equals(login) && customer.getEmail().equals(email)) {
                customer.setStatus("delete");
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
            if (customer.getLogin().equals(login) && customer.getEmail().equals(email)) {
                customer.setStatus("active");
                return customerDAO.save(customer);
            }
        }
        throw new RuntimeException("active error");
    }

    @RequestMapping(value = "/customer/create", method = RequestMethod.POST)
    public @ResponseBody
    Customer create(@RequestParam(name = "login") String login, @RequestParam(name = "email") String email) {
        Iterable<Customer> customers = customerDAO.findAll();
        for (Customer customer : customers) {
            if (customer.getLogin().equals(login) || customer.getEmail().equals(email)) {
                throw new RuntimeException("Create customer error");
            }
        }
        Customer customer = new Customer();
        customer.setEmail(email);
        customer.setLogin(login);
        customer.setCategory("default");
        customer.setRegistration(Instant.now());
        customer.setStatus("active");
        return customerDAO.save(customer);
    }
}
