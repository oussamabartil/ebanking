package ma.enset.ebancking;

import ma.enset.ebancking.entities.Customer;
import ma.enset.ebancking.repositories.AccountOperationRepository;
import ma.enset.ebancking.repositories.BankAccountRepository;
import ma.enset.ebancking.repositories.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.stream.Stream;

@SpringBootApplication
public class EbanckingApplication {

    public static void main(String[] args) {
        SpringApplication.run(EbanckingApplication.class, args);
    }

    @Bean
    CommandLineRunner start (CustomerRepository customerRepository,
                             BankAccountRepository bankAccountRepository,
                             AccountOperationRepository accountOperationRepository){
        return  args -> {
            Stream.of("Hassan","Yassine","Aicha").forEach(name-> {
                Customer customer = new Customer();
                customer.setName(name);
                customer.setEmail(name+"@gmail.com");
                customerRepository.save(customer);
            });
        };
    }
}
