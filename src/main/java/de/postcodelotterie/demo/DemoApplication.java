package de.postcodelotterie.demo;

import de.postcodelotterie.demo.model.Customer;
import de.postcodelotterie.demo.dao.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Bean
    public CommandLineRunner init(CustomerRepository customerRepository){
        return args ->{
        };
    }
}
