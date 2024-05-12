package ma.enset.ebankingapp.services;

import ma.enset.ebankingapp.dtos.*;
import ma.enset.ebankingapp.entities.CurrentAccount;
import ma.enset.ebankingapp.entities.Customer;
import ma.enset.ebankingapp.entities.SavingAccount;
import ma.enset.ebankingapp.entities.bankAccount;
import ma.enset.ebankingapp.exceptions.BalanceNotSufficient;
import ma.enset.ebankingapp.exceptions.BankAccountNotFoundException;
import ma.enset.ebankingapp.exceptions.CustomerNotFoundException;

import java.util.List;

public interface BankAccountService {
     CustomerDTO saveCustomer(CustomerDTO customerDTO);
     CurrentbankAccountDTO saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException;
     SavingbankAccountDTO saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException;

     List<CustomerDTO> listCustomers();
     BankAccountDTO getBankAccount(String bankAccountId) throws BankAccountNotFoundException;
     void debit(String accountId,double amount,String description) throws BankAccountNotFoundException, BalanceNotSufficient;
     void credit(String accountId,double amount,String description) throws BankAccountNotFoundException;
     void transfer(String accountIdSource,String accountIdDest,double amountOperation) throws BalanceNotSufficient, BankAccountNotFoundException;
     List<BankAccountDTO> bankAcountsList();

     CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException;

     CustomerDTO updateCustomer(CustomerDTO customerDTO);

     void deleteCustomer(Long customerId);

     //pour operations :
     List<AccountOperationDTO> accountHistory(String accountId);

     AccountHistoryDTO getAccountHistory(String accountId, int page, int size) throws BankAccountNotFoundException;
     List<CustomerDTO> searchCustomers(String keyword);

}
