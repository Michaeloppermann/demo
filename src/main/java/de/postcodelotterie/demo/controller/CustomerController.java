package de.postcodelotterie.demo.controller;

import de.postcodelotterie.demo.model.Customer;
import de.postcodelotterie.demo.dao.CustomerRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/customers")
public class CustomerController {

    private final CustomerRepository customerRepository;

    CustomerController(CustomerRepository customerRepository){
        this.customerRepository = customerRepository;
    }

    @GetMapping()
    List<Customer> getAllCustomers(){
        return customerRepository.findAll();
    }

    @PostMapping()
    public Customer saveCustomer(@RequestBody Customer customer) {
        return customerRepository.save(customer);
    }

    @DeleteMapping("/{id}")
    public void deleteCustomerById(@PathVariable Long id) {
        customerRepository.deleteById(id);
    }

    @GetMapping("/{id}")
    public Customer getCustomerById(@PathVariable Long id) {
         return customerRepository.findById(id).orElse(null);
    }
}
