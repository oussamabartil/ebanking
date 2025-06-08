package ma.enset.ebancking.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.enset.ebancking.dtos.DashboardStatsDTO;
import ma.enset.ebancking.entities.AccountOperation;
import ma.enset.ebancking.entities.BankAccount;
import ma.enset.ebancking.entities.Customer;
import ma.enset.ebancking.enumes.OperationType;
import ma.enset.ebancking.repositories.BankAccountRepository;
import ma.enset.ebancking.repositories.CustomerRepository;
import ma.enset.ebancking.repositories.AccountOperationRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class DashboardServiceImpl implements DashboardService {
    
    private CustomerRepository customerRepository;
    private BankAccountRepository bankAccountRepository;
    private AccountOperationRepository accountOperationRepository;
    
    @Override
    public DashboardStatsDTO getDashboardStatistics() {
        return getDashboardStatistics("monthly");
    }
    
    @Override
    public DashboardStatsDTO getDashboardStatistics(String period) {
        DashboardStatsDTO stats = new DashboardStatsDTO();
        
        // Basic counts
        stats.setTotalCustomers(customerRepository.count());
        stats.setTotalAccounts(bankAccountRepository.count());
        stats.setTotalTransactions(accountOperationRepository.count());
        
        // Financial summaries
        List<BankAccount> accounts = bankAccountRepository.findAll();
        BigDecimal totalBalance = accounts.stream()
                .map(BankAccount::getBalance)
                .map(BigDecimal::valueOf)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        stats.setTotalBalance(totalBalance);
        
        // Transaction summaries
        List<AccountOperation> operations = accountOperationRepository.findAll();
        BigDecimal totalCredits = operations.stream()
                .filter(op -> op.getType() == OperationType.CREDIT)
                .map(AccountOperation::getAmount)
                .map(BigDecimal::valueOf)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalDebits = operations.stream()
                .filter(op -> op.getType() == OperationType.DEBIT)
                .map(AccountOperation::getAmount)
                .map(BigDecimal::valueOf)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        stats.setTotalCredits(totalCredits);
        stats.setTotalDebits(totalDebits);
        
        // Growth rates (simplified calculation)
        stats.setCustomerGrowthRate(calculateGrowthRate("customers", period));
        stats.setTransactionGrowthRate(calculateGrowthRate("transactions", period));
        
        // Recent transactions (last 10)
        List<AccountOperation> recentOps = accountOperationRepository
                .findTop10ByOrderByOperationDateDesc();
        stats.setRecentTransactions(mapToRecentTransactions(recentOps));
        
        // Top customers by balance
        stats.setTopCustomers(getTopCustomers());
        
        // Chart data
        stats.setTransactionsByMonth(getTransactionsByMonth());
        stats.setBalanceByAccountType(getBalanceByAccountType());
        stats.setCustomersByMonth(getCustomersByMonth());
        
        return stats;
    }
    
    private Double calculateGrowthRate(String type, String period) {
        // Simplified growth rate calculation
        // In a real implementation, you would compare current period with previous period
        return Math.random() * 20 - 10; // Random growth rate between -10% and +10%
    }
    
    private List<DashboardStatsDTO.RecentTransactionDTO> mapToRecentTransactions(List<AccountOperation> operations) {
        return operations.stream().map(op -> {
            DashboardStatsDTO.RecentTransactionDTO dto = new DashboardStatsDTO.RecentTransactionDTO();
            dto.setId(op.getId());
            dto.setType(op.getType().toString());
            dto.setAmount(BigDecimal.valueOf(op.getAmount()));
            dto.setAccountId(op.getBankAccount().getId());
            dto.setCustomerName(op.getBankAccount().getCustomer().getName());
            dto.setDate(op.getOperationDate().toString());
            dto.setDescription(op.getDescription());
            return dto;
        }).collect(Collectors.toList());
    }
    
    private List<DashboardStatsDTO.CustomerStatsDTO> getTopCustomers() {
        List<Customer> customers = customerRepository.findAll();
        return customers.stream()
                .map(customer -> {
                    DashboardStatsDTO.CustomerStatsDTO dto = new DashboardStatsDTO.CustomerStatsDTO();
                    dto.setCustomerId(customer.getId());
                    dto.setCustomerName(customer.getName());
                    dto.setEmail(customer.getEmail());
                    
                    List<BankAccount> customerAccounts = customer.getBankAccounts();
                    dto.setAccountsCount((long) customerAccounts.size());
                    
                    BigDecimal totalBalance = customerAccounts.stream()
                            .map(BankAccount::getBalance)
                            .map(BigDecimal::valueOf)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    dto.setTotalBalance(totalBalance);
                    
                    Long transactionsCount = customerAccounts.stream()
                            .mapToLong(account -> account.getAccountOperations().size())
                            .sum();
                    dto.setTransactionsCount(transactionsCount);
                    
                    return dto;
                })
                .sorted((a, b) -> b.getTotalBalance().compareTo(a.getTotalBalance()))
                .limit(5)
                .collect(Collectors.toList());
    }
    
    private Map<String, Long> getTransactionsByMonth() {
        Map<String, Long> result = new LinkedHashMap<>();
        LocalDate now = LocalDate.now();
        
        for (int i = 5; i >= 0; i--) {
            LocalDate month = now.minusMonths(i);
            String monthKey = month.format(DateTimeFormatter.ofPattern("MMM yyyy"));
            
            // Count transactions for this month
            LocalDateTime startOfMonth = month.withDayOfMonth(1).atStartOfDay();
            LocalDateTime endOfMonth = month.withDayOfMonth(month.lengthOfMonth()).atTime(23, 59, 59);
            
            Long count = accountOperationRepository.countByOperationDateBetween(startOfMonth, endOfMonth);
            result.put(monthKey, count);
        }
        
        return result;
    }
    
    private Map<String, BigDecimal> getBalanceByAccountType() {
        Map<String, BigDecimal> result = new HashMap<>();
        List<BankAccount> accounts = bankAccountRepository.findAll();
        
        result.put("Current Account", accounts.stream()
                .filter(account -> account.getClass().getSimpleName().equals("CurrentAccount"))
                .map(BankAccount::getBalance)
                .map(BigDecimal::valueOf)
                .reduce(BigDecimal.ZERO, BigDecimal::add));

        result.put("Saving Account", accounts.stream()
                .filter(account -> account.getClass().getSimpleName().equals("SavingAccount"))
                .map(BankAccount::getBalance)
                .map(BigDecimal::valueOf)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        
        return result;
    }
    
    private Map<String, Long> getCustomersByMonth() {
        Map<String, Long> result = new LinkedHashMap<>();
        LocalDate now = LocalDate.now();
        
        for (int i = 5; i >= 0; i--) {
            LocalDate month = now.minusMonths(i);
            String monthKey = month.format(DateTimeFormatter.ofPattern("MMM yyyy"));
            
            // This is a simplified version - in reality you'd track customer creation dates
            Long count = customerRepository.count() / 6; // Distribute evenly across months
            result.put(monthKey, count);
        }
        
        return result;
    }
}
