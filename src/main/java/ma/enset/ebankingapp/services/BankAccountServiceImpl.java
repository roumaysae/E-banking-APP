package ma.enset.ebankingapp.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.enset.ebankingapp.dtos.*;
import ma.enset.ebankingapp.entities.*;
import ma.enset.ebankingapp.enums.OperationType;
import ma.enset.ebankingapp.exceptions.BalanceNotSufficient;
import ma.enset.ebankingapp.exceptions.BankAccountNotFoundException;
import ma.enset.ebankingapp.exceptions.CustomerNotFoundException;
import ma.enset.ebankingapp.mappers.bankAccountMapperImpl;
import ma.enset.ebankingapp.repositories.BankingAccountRepository;
import ma.enset.ebankingapp.repositories.CustomerRepository;
import ma.enset.ebankingapp.repositories.OperationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.print.Pageable;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@AllArgsConstructor
public
class BankAccountServiceImpl implements BankAccountService {
    private CustomerRepository customerRepository;
    private BankingAccountRepository bankingAccountRepository;
    private OperationRepository operationRepository;
    private bankAccountMapperImpl dtoMapper;


    @Override
    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
        log.info("Saving new Customer");
        Customer customer=dtoMapper.toCustomer(customerDTO);
        Customer savedCustomer = customerRepository.save(customer);
        return dtoMapper.toCustomerDTO(savedCustomer);
    }

    @Override
    public CurrentbankAccountDTO saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId).orElse(null);
        if (customer == null)
            throw new CustomerNotFoundException("Customer not found");
        CurrentAccount currentAccount = new CurrentAccount();
        currentAccount.setId(UUID.randomUUID().toString());
        currentAccount.setCreatedDate(new Date());
        currentAccount.setBalance(initialBalance);
        currentAccount.setOverdraft(overDraft);
        currentAccount.setCustomer(customer);
        CurrentAccount savedBankAccount = bankingAccountRepository.save(currentAccount);
        return dtoMapper.toCurrentBanKAccountDTO(savedBankAccount);
    }

    @Override
    public SavingbankAccountDTO saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId).orElse(null);
        if (customer == null)
            throw new CustomerNotFoundException("Customer not found");
        SavingAccount savingAccount = new SavingAccount();
        savingAccount.setId(UUID.randomUUID().toString());
        savingAccount.setCreatedDate(new Date());
        savingAccount.setBalance(initialBalance);
        savingAccount.setInterestRate(interestRate);
        savingAccount.setCustomer(customer);
        SavingAccount savedBankAccount = bankingAccountRepository.save(savingAccount);
        return dtoMapper.toSavingBankAccountDTO(savedBankAccount);
    }

    @Override
    public List<CustomerDTO> listCustomers() {
        List<Customer> customers = customerRepository.findAll();
        List<CustomerDTO> customerDTOS = customers.stream()
                .map(customer -> dtoMapper.toCustomerDTO(customer))
                .collect(Collectors.toList());
        return customerDTOS;
    }


    //consulter un compte :
    @Override
    public BankAccountDTO getBankAccount(String bankAccountId) throws BankAccountNotFoundException {
        bankAccount bankAccount = bankingAccountRepository.findById(bankAccountId).
                orElseThrow(() -> new BankAccountNotFoundException("bankAccount not found"));

        if (bankAccount instanceof SavingAccount savingAccount) {
            return dtoMapper.toSavingBankAccountDTO(savingAccount);
        } else {
            CurrentAccount currentAccount = (CurrentAccount) bankAccount;
            return dtoMapper.toCurrentBanKAccountDTO(currentAccount);
        }

    }

    @Override
    public void debit(String accountId, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficient {
        bankAccount bankAccount = bankingAccountRepository.findById(accountId).
                orElseThrow(() -> new BankAccountNotFoundException("bankAccount not found"));
        if (bankAccount.getBalance() < amount) {
            throw new BalanceNotSufficient("Balance is not sufficient!!! ");
        }
        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setType(OperationType.DEBIT);
        accountOperation.setAmount(amount);
        accountOperation.setOperationDate(new Date());
        accountOperation.setDescription(description);
        operationRepository.save(accountOperation);//creer l'operation de debit et l'enregistrer sous operationrepository

        bankAccount.setBalance(bankAccount.getBalance() - amount);//retrancher le montant de l'operation et le mettre à jour

        bankingAccountRepository.save(bankAccount);//enregistrer le nouveau  compte


    }

    @Override
    public void credit(String accountId, double amount, String description) throws BankAccountNotFoundException {
        bankAccount bankAccount = bankingAccountRepository.findById(accountId).
                orElseThrow(() -> new BankAccountNotFoundException("bankAccount not found"));

        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setType(OperationType.CREDIT);
        accountOperation.setAmount(amount);
        accountOperation.setOperationDate(new Date());
        accountOperation.setDescription(description);
        operationRepository.save(accountOperation);//creer l'operation de debit et l'enregistrer sous operationrepository

        bankAccount.setBalance(bankAccount.getBalance() + amount);//ajouter le montant de l'operation au solde courant au niveau de  compte et le mettre à jour

        bankingAccountRepository.save(bankAccount);//enregistrer le nouveau  compte

    }

    //dans ces deux derniers methodes en utilise pas el DTO car on a pas des types complexes , on a des types simples de void vers void
    @Override
    public void transfer(String accountIdSource, String accountIdDest, double amountOperation) throws BalanceNotSufficient, BankAccountNotFoundException {
        debit(accountIdSource, amountOperation, "transfer from the source account");
        credit(accountIdDest, amountOperation, "transfer to the destination account");
    }


    @Override
    public List<BankAccountDTO> bankAcountsList() {
        List<bankAccount> bankAccounts = bankingAccountRepository.findAll();
        List<BankAccountDTO> bankAccountDTOS = bankAccounts.stream().map(bankAccount -> {
            if (bankAccount instanceof SavingAccount) {
                SavingAccount savingAccount = (SavingAccount) bankAccount;
                return dtoMapper.toSavingBankAccountDTO(savingAccount);
            } else {
                CurrentAccount currentAccount = (CurrentAccount) bankAccount;
                return dtoMapper.toCurrentBanKAccountDTO(currentAccount);
            }
        }).collect(Collectors.toList());
        return bankAccountDTOS;
    }

    @Override
    public CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId).orElseThrow(() ->
                new CustomerNotFoundException("Customer not found "));
        return dtoMapper.toCustomerDTO(customer);
    }

    @Override
    public CustomerDTO updateCustomer(CustomerDTO customerDTO) {
        log.info("Saving new Customer");
        Customer customer = dtoMapper.toCustomer(customerDTO);
        Customer savedCustomer = customerRepository.save(customer);
        return dtoMapper.toCustomerDTO(savedCustomer);
    }

    @Override
    public void deleteCustomer(Long customerId) {
        customerRepository.deleteById(customerId);
    }

    //pour operations :
    @Override
    public List<AccountOperationDTO> accountHistory(String accountId) {
        List<AccountOperation> accountOperationList = operationRepository.findByBankAccountId(accountId);
        return accountOperationList.stream().map(accountOp ->
                dtoMapper.toAccountOperationDTO(accountOp)).collect(Collectors.toList());
    }


    @Override
    public AccountHistoryDTO getAccountHistory(String accountId, int page, int size) throws BankAccountNotFoundException {
        bankAccount bankAccount = bankingAccountRepository.findById(accountId).orElse(null);

        if (bankAccount == null) throw new BankAccountNotFoundException("Account not Found");
//if not :
        Page<AccountOperation> accountOperations = operationRepository.findByBankAccountIdOrderByOperationDateDesc(accountId, PageRequest.of(page, size));
        AccountHistoryDTO accountHistoryDTO = new AccountHistoryDTO();

        List<AccountOperationDTO> accountOperationDTOS = accountOperations.getContent().stream()
                .map(op -> dtoMapper.toAccountOperationDTO(op)).collect(Collectors.toList());

        accountHistoryDTO.setAccountOperationDTOS(accountOperationDTOS);
        accountHistoryDTO.setAccountId(bankAccount.getId());
        accountHistoryDTO.setBalance(bankAccount.getBalance());
        accountHistoryDTO.setCurrentPage(page);
        accountHistoryDTO.setPageSize(size);
        accountHistoryDTO.setTotalPages(accountOperations.getTotalPages());
        return accountHistoryDTO;
    }

    @Override
    public List<CustomerDTO> searchCustomers(String keyword) {
       List<Customer> customers = customerRepository.searchCustomer(keyword);
       List<CustomerDTO> customerDTOS = customers.stream().map(cs ->
       dtoMapper.toCustomerDTO(cs)).collect(Collectors.toList());
        return customerDTOS;
    }
}