package ma.enset.ebancking.services;

import ma.enset.ebancking.entities.BankAccount;
import ma.enset.ebancking.entities.CurrentAccount;
import ma.enset.ebancking.entities.Customer;
import ma.enset.ebancking.entities.SavingAccount;
import ma.enset.ebancking.exceptions.BalanceNotSufficientException;
import ma.enset.ebancking.exceptions.BankAccountNotFoundException;
import ma.enset.ebancking.exceptions.CustomerNotFoundException;

import java.util.List;

public interface BankAccountService {
    Customer saveCustomer(Customer customer);
    CurrentAccount saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException;
   SavingAccount saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException;
    List<Customer> listCustomers();
    BankAccount getBankAccount(String accountId) throws BankAccountNotFoundException;
    void debit(String accountId, double amount, String description) throws BalanceNotSufficientException, BankAccountNotFoundException;
    void credit(String accountId, double amount, String description) throws BankAccountNotFoundException;
    void transfer(String accountIdSource, String accountIdDestination, Double amount) throws BankAccountNotFoundException, BalanceNotSufficientException;
}
