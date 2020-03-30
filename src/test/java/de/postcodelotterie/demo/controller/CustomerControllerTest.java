package de.postcodelotterie.demo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.postcodelotterie.demo.dao.CustomerRepository;
import de.postcodelotterie.demo.model.Customer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = CustomerController.class)
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CustomerRepository customerRepository;

    @Test
    void getAllCustomers() throws Exception {

        MvcResult mvcResult = mockMvc.perform(get("/customers")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        verify(customerRepository, times(1)).findAll();

    }

    @Test
    void saveCustomer() throws Exception {
        Customer customer = new Customer("Michael", "Oppermann", LocalDate.of(1985, 11, 19));

        mockMvc.perform(post("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customer)))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();


        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerRepository, times(1)).save(customerArgumentCaptor.capture());
        Customer actualCustomer = customerArgumentCaptor.getValue();

        assertThat(actualCustomer.getLastName()).isEqualTo(customer.getLastName());
        assertThat(actualCustomer.getFirstName()).isEqualTo(customer.getFirstName());
        assertThat(actualCustomer.getBirthday()).isEqualTo(customer.getBirthday());

    }

    @Test
    void deleteCustomerById() throws Exception {
        Long id = 1L;

        mockMvc.perform(delete("/customers/{id}", id))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        verify(customerRepository, times(1)).deleteById(1L);
    }

    @Test
    void getCustomerById() throws Exception {
        Long id = 1L;

        mockMvc.perform(get("/customers/{id}", id))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        verify(customerRepository, times(1)).findById(id);

    }
}