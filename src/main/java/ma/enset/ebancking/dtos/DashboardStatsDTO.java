package ma.enset.ebancking.dtos;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
public class DashboardStatsDTO {
    private Long totalCustomers;
    private Long totalAccounts;
    private Long totalTransactions;
    private BigDecimal totalBalance;
    private BigDecimal totalCredits;
    private BigDecimal totalDebits;
    private Double customerGrowthRate;
    private Double transactionGrowthRate;
    
    // Recent activity
    private List<RecentTransactionDTO> recentTransactions;
    private List<CustomerStatsDTO> topCustomers;
    
    // Chart data
    private Map<String, Long> transactionsByMonth;
    private Map<String, BigDecimal> balanceByAccountType;
    private Map<String, Long> customersByMonth;
    
    @Data
    public static class RecentTransactionDTO {
        private Long id;
        private String type;
        private BigDecimal amount;
        private String accountId;
        private String customerName;
        private String date;
        private String description;
    }
    
    @Data
    public static class CustomerStatsDTO {
        private Long customerId;
        private String customerName;
        private String email;
        private Long accountsCount;
        private BigDecimal totalBalance;
        private Long transactionsCount;
    }
}
