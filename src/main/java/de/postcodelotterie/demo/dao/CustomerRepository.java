package de.postcodelotterie.demo.dao;

import de.postcodelotterie.demo.model.Customer;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CustomerRepository extends CrudRepository<Customer, Long> {

    List<Customer> findAll();

    List<Customer> findByLastName(String lastname);

    Customer findById(long id);
}
