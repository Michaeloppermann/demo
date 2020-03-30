package de.postcodelotterie.demo;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.postcodelotterie.demo.controller.CustomerController;
import de.postcodelotterie.demo.dao.CustomerRepository;
import de.postcodelotterie.demo.model.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DemoApplicationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private CustomerController controller;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TestRestTemplate testRestTemplate;

    private static final String LOCAL_HOST = "http://localHost:";
    private static final String DOMAIN = "/customers";

    @Test
    void contextLoads() {
        assertThat(controller).isNotNull();
        assertThat(customerRepository).isNotNull();
    }

    @Test
    public void shouldReturnEmptyListOfCustomers() {
        customerRepository.deleteAll();
        assertThat(customerRepository.findAll()).isEmpty();

        ResponseEntity<String> response = testRestTemplate.getForEntity(LOCAL_HOST + port + DOMAIN, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("[]");
    }

    @Test
    public void shouldReturnFilledListOfCustomers() throws Exception {
        customerRepository.deleteAll();
        assertThat(customerRepository.findAll()).isEmpty();

        List<Customer> customers = Arrays.asList(
                new Customer("Michael", "Oppermann", LocalDate.of(1985, 11, 19)),
                new Customer("Peter", "Shaw", LocalDate.of(1982, 6, 12)),
                new Customer("Bob", "Andrews", LocalDate.of(1983, 2, 24))
        );
        customerRepository.saveAll(customers);

        ResponseEntity<String> response = testRestTemplate.getForEntity(LOCAL_HOST + port + DOMAIN, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();

        List<Customer> actualCustomers = objectMapper.readValue(response.getBody(), new TypeReference<List<Customer>>() {
        });
        assertThat(actualCustomers).isEqualTo(customers);
    }

    @Test
    public void shouldAddCustomerToRepo() {
        customerRepository.deleteAll();
        assertThat(customerRepository.findAll()).isEmpty();

        Customer customer = new Customer("Michael", "Oppermann", LocalDate.of(1985, 11, 19));
        HttpEntity<Customer> request = new HttpEntity<>(customer);

        Customer addedCustomer = testRestTemplate.postForObject(LOCAL_HOST + port + DOMAIN, request, Customer.class);
        addedCustomer.setId(null);
        assertThat(addedCustomer).isEqualTo(customer);

        List<Customer> actualCustomers = customerRepository.findAll();
        assertThat(actualCustomers).hasSize(1);

        Customer customerFromRepo = actualCustomers.get(0);
        customerFromRepo.setId(null);
        assertThat(customerFromRepo).isEqualTo(customer);
    }

    @Test
    public void shouldDeleteCustomer() {
        customerRepository.deleteAll();
        assertThat(customerRepository.findAll()).isEmpty();

        Customer customer = new Customer("Michael", "Oppermann", LocalDate.of(1985, 11, 19));
        customerRepository.save(customer);
        assertThat(customerRepository.findAll()).hasSize(1);

        String entityUrl = DOMAIN + "/" + customerRepository.findAll().get(0).getId();
        testRestTemplate.delete(entityUrl);

        assertThat(customerRepository.findAll()).isEmpty();
    }

    @Test
    public void shouldUpdateCustomer() {
        customerRepository.deleteAll();
        assertThat(customerRepository.findAll()).isEmpty();

        Customer customer = new Customer("Michael", "Oppermann", LocalDate.of(1985, 11, 19));
        customerRepository.save(customer);
        assertThat(customerRepository.findAll()).hasSize(1);

        customer = customerRepository.findAll().get(0);
        customer.setLastName("Müller");

        HttpEntity<Customer> request = new HttpEntity<>(customer);
        Customer addedCustomer = testRestTemplate.postForObject(LOCAL_HOST + port + DOMAIN, request, Customer.class);

        assertThat(addedCustomer).isEqualTo(customer);
        assertThat(customerRepository.findAll()).hasSize(1);

        Customer customerFromRepo = customerRepository.findAll().get(0);
        assertThat(customerFromRepo).isEqualTo(customer);
    }

}
