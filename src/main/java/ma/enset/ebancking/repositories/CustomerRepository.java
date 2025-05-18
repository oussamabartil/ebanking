package ma.enset.ebancking.repositories;

import ma.enset.ebancking.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
