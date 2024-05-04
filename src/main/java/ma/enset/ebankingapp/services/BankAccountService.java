package ma.enset.ebankingapp.services;

import ma.enset.ebankingapp.entities.CurrentAccount;
import ma.enset.ebankingapp.entities.Customer;
import ma.enset.ebankingapp.entities.SavingAccount;
import ma.enset.ebankingapp.entities.bankAccount;
import ma.enset.ebankingapp.exceptions.BalanceNotSufficient;
import ma.enset.ebankingapp.exceptions.BankAccountNotFoundException;

import java.util.List;

public interface BankAccountService {
     Customer saveCustomer(Customer customer);
     CurrentAccount saveCurrentBankAccount(double initialBalance,double overDraft,Long customerId);
     SavingAccount saveSavingBankAccount(double initialBalance, double interestRate , Long customerId);
     List<Customer> listCustomers();
     bankAccount getBankAccount(String bankAccountId) throws BankAccountNotFoundException;
     void debit(String accountId,double amount,String description) throws BankAccountNotFoundException, BalanceNotSufficient;
     void credit(String accountId,double amount,String description) throws BankAccountNotFoundException;
     void transfer(String accountIdSource,String accountIdDest,double amountOperation) throws BalanceNotSufficient, BankAccountNotFoundException;
     List<bankAccount> bankAcountsList();

}
