package ma.enset.ebankingapp.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.enset.ebankingapp.entities.*;
import ma.enset.ebankingapp.enums.OperationType;
import ma.enset.ebankingapp.exceptions.BalanceNotSufficient;
import ma.enset.ebankingapp.exceptions.BankAccountNotFoundException;
import ma.enset.ebankingapp.exceptions.CustomerNotFoundException;
import ma.enset.ebankingapp.repositories.BankingAccountRepository;
import ma.enset.ebankingapp.repositories.CustomerRepository;
import ma.enset.ebankingapp.repositories.OperationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class BankAccountServiceImpl implements BankAccountService {
    private CustomerRepository customerRepository;
    private BankingAccountRepository bankingAccountRepository;
    private OperationRepository operationRepository;

    @Override
    public Customer saveCustomer(Customer customer) {
        log.info("saving a customer");
        Customer savedCustomer = customerRepository.save(customer);
        return savedCustomer;
    }//on cree un client


    @Override
    public SavingAccount saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) {
        Customer customer = customerRepository.findById(customerId).get();

        if(customer==null){
            try {
                throw new CustomerNotFoundException("Customer Not found!!!");//if faut creer nos propres exceptions dans le package exception
            } catch (CustomerNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        SavingAccount savingAccount = new SavingAccount();
        savingAccount.setId(UUID.randomUUID().toString());
        savingAccount.setBalance(initialBalance);
        savingAccount.setCreatedDate(new Date());
        savingAccount.setInterestRate(interestRate);
        savingAccount.setCustomer(customer);
        return  bankingAccountRepository.save(savingAccount);
    }

    @Override
    public CurrentAccount saveCurrentBankAccount(double initialBalance, double overDraft , Long customerId) {
        Customer customer = customerRepository.findById(customerId).get();

        if(customer==null){
            try {
                throw new CustomerNotFoundException("Customer Not found!!!");//if faut creer nos propres exceptions dans le package exception
            } catch (CustomerNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
             CurrentAccount currentAccount = new CurrentAccount();
        currentAccount.setId(UUID.randomUUID().toString());
        currentAccount.setBalance(initialBalance);
        currentAccount.setCreatedDate(new Date());
        currentAccount.setOverdraft(overDraft);
        currentAccount.setCustomer(customer);
          CurrentAccount currentAccountSaved= bankingAccountRepository.save(currentAccount);
                return currentAccountSaved;
}

    @Override
    public List<Customer> listCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public bankAccount getBankAccount(String bankAccountId) throws BankAccountNotFoundException {
        bankAccount bankAccount = bankingAccountRepository.findById(bankAccountId).
                orElseThrow(()-> new BankAccountNotFoundException("bankAccount not found"));
                return bankAccount;
    }

    @Override
    public void debit(String accountId, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficient {
        bankAccount bankAccount = getBankAccount(accountId);
 if(bankAccount.getBalance()<amount){
     throw new BalanceNotSufficient("Balance is not sufficient!!! ");
 }
                AccountOperation accountOperation = new AccountOperation();
         accountOperation.setType(OperationType.DEBIT);
         accountOperation.setAmount(amount);
         accountOperation.setOperationDate(new Date());
         accountOperation.setDescription(description);
         operationRepository.save(accountOperation);//creer l'operation de debit et l'enregistrer sous operationrepository

         bankAccount.setBalance(bankAccount.getBalance()-amount);//retrancher le montant de l'operation et le mettre à jour

        bankingAccountRepository.save(bankAccount);//enregistrer le nouveau  compte


    }

    @Override
    public void credit(String accountId, double amount, String description) throws BankAccountNotFoundException {
        bankAccount bankAccount = getBankAccount(accountId);

        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setType(OperationType.CREDIT);
        accountOperation.setAmount(amount);
        accountOperation.setOperationDate(new Date());
        accountOperation.setDescription(description);
        operationRepository.save(accountOperation);//creer l'operation de debit et l'enregistrer sous operationrepository

        bankAccount.setBalance(bankAccount.getBalance()+ amount);//ajouter le montant de l'operation au solde courant au niveau de  compte et le mettre à jour

        bankingAccountRepository.save(bankAccount);//enregistrer le nouveau  compte

    }

    @Override
    public void transfer(String accountIdSource, String accountIdDest, double amountOperation) throws BalanceNotSufficient, BankAccountNotFoundException {
        debit(accountIdSource,amountOperation,"transfer from the source account");
        credit(accountIdDest,amountOperation,"transfer to the destination account");
    }

    @Override
    public List<bankAccount> bankAcountsList() {
        return bankingAccountRepository.findAll();
    }
}

