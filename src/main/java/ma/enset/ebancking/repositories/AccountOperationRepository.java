package ma.enset.ebancking.repositories;

import ma.enset.ebancking.entities.AccountOperation;
import ma.enset.ebancking.entities.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountOperationRepository extends JpaRepository<AccountOperation, Long> {
}
