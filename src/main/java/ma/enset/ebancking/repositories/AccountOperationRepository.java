package ma.enset.ebancking.repositories;

import ma.enset.ebancking.entities.AccountOperation;
import ma.enset.ebancking.entities.BankAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface AccountOperationRepository extends JpaRepository<AccountOperation, Long> {
    List<AccountOperation> findByBankAccountId(String accountId);

    Page<AccountOperation> findByBankAccountId(String accountId, Pageable pageable);

    // Dashboard specific queries
    List<AccountOperation> findTop10ByOrderByOperationDateDesc();

    Long countByOperationDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT COUNT(o) FROM AccountOperation o WHERE o.operationDate >= :startDate")
    Long countOperationsAfterDate(LocalDateTime startDate);
}
