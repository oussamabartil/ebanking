package ma.enset.ebancking;

import ma.enset.ebancking.entities.CurrentAccount;
import ma.enset.ebancking.entities.Customer;
import ma.enset.ebancking.entities.SavingAccount;
import ma.enset.ebancking.enumes.AccountStatus;
import ma.enset.ebancking.repositories.AccountOperationRepository;
import ma.enset.ebancking.repositories.BankAccountRepository;
import ma.enset.ebancking.repositories.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.UUID;
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

            customerRepository.findAll().forEach(cust->{
                CurrentAccount currentAccount = new CurrentAccount();
                currentAccount.setId(UUID.randomUUID().toString());
                currentAccount.setBalance(Math.random()*90000);
                currentAccount.setCreatedAt(new Date());
                currentAccount.setStatus(AccountStatus.CREATED);
                currentAccount.setCustomer(cust);
                currentAccount.setOverDraft(9000);
                bankAccountRepository.save(currentAccount);

                SavingAccount savingAccount = new SavingAccount();
                savingAccount.setId(UUID.randomUUID().toString());
                savingAccount.setBalance(Math.random()*90000);
                savingAccount.setCreatedAt(new Date());
                savingAccount.setStatus(AccountStatus.CREATED);
                savingAccount.setCustomer(cust);
                savingAccount.setInterestRate(5.5);
                bankAccountRepository.save(savingAccount);


            });
        };
    }


}
