package ma.enset.ebankingapp;

import ma.enset.ebankingapp.dtos.BankAccountDTO;
import ma.enset.ebankingapp.dtos.CurrentbankAccountDTO;
import ma.enset.ebankingapp.dtos.CustomerDTO;
import ma.enset.ebankingapp.dtos.SavingbankAccountDTO;
import ma.enset.ebankingapp.entities.*;
import ma.enset.ebankingapp.enums.AccountStatus;
import ma.enset.ebankingapp.enums.OperationType;
import ma.enset.ebankingapp.exceptions.BalanceNotSufficient;
import ma.enset.ebankingapp.exceptions.BankAccountNotFoundException;
import ma.enset.ebankingapp.exceptions.CustomerNotFoundException;
import ma.enset.ebankingapp.repositories.BankingAccountRepository;
import ma.enset.ebankingapp.repositories.OperationRepository;
import ma.enset.ebankingapp.repositories.CustomerRepository;
import ma.enset.ebankingapp.services.BankAccountService;
import ma.enset.ebankingapp.services.BankAccountServiceImpl;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class EbankingAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(EbankingAppApplication.class, args);
    }
    @Bean
    CommandLineRunner commandLineRunner(BankAccountService bankAccountService){
        return args -> {
            Stream.of("ROUMAYSAE","Imane","SALIM").forEach(name->{
                CustomerDTO customer=new CustomerDTO();
                customer.setName(name);
                customer.setEmail(name+"@gmail.com");
                bankAccountService.saveCustomer(customer);
            });
            bankAccountService.listCustomers().forEach(customer->{
                try {
                    bankAccountService.saveCurrentBankAccount(Math.random()*1000000,9000,customer.getId());
                    bankAccountService.saveSavingBankAccount(Math.random()*120000,5.5,customer.getId());

                } catch (CustomerNotFoundException e) {
                    e.printStackTrace();
                }
            });

            List<BankAccountDTO> bankAccounts = bankAccountService.bankAcountsList();

            for (BankAccountDTO bankAccount:bankAccounts){
                for (int i = 0; i <10 ; i++) {
                    String accountId;
                    if(bankAccount instanceof SavingbankAccountDTO){
                        accountId=((SavingbankAccountDTO) bankAccount).getId();
                    } else{
                        accountId=((CurrentbankAccountDTO) bankAccount).getId();
                    }
                    bankAccountService.credit(accountId,10000+Math.random()*120000,"Credit");
                    bankAccountService.debit(accountId,1000+Math.random()*9000,"Debit");
                }
            }
        };
    }
    //@Bean
    CommandLineRunner start(CustomerRepository customerRepository,
                            BankingAccountRepository bankAccountRepository,
                            OperationRepository accountOperationRepository){
        return args -> {
            Stream.of("Hassan","Yassine","Aicha").forEach(name->{
                Customer customer=new Customer();
                customer.setName(name);
                customer.setEmail(name+"@gmail.com");
                customerRepository.save(customer);
            });
            customerRepository.findAll().forEach(cust->{
                CurrentAccount currentAccount=new CurrentAccount();
                currentAccount.setId(UUID.randomUUID().toString());
                currentAccount.setBalance(Math.random()*90000);
                currentAccount.setCreatedDate(new Date());
                currentAccount.setStatus(AccountStatus.CREATED);
                currentAccount.setCustomer(cust);
                currentAccount.setOverdraft(9000);
                bankAccountRepository.save(currentAccount);

                SavingAccount savingAccount=new SavingAccount();
                savingAccount.setId(UUID.randomUUID().toString());
                savingAccount.setBalance(Math.random()*90000);
                savingAccount.setCreatedDate(new Date());
                savingAccount.setStatus(AccountStatus.CREATED);
                savingAccount.setCustomer(cust);
                savingAccount.setInterestRate(5.5);
                bankAccountRepository.save(savingAccount);

            });
            bankAccountRepository.findAll().forEach(acc->{
                for (int i = 0; i <10 ; i++) {
                    AccountOperation accountOperation=new AccountOperation();
                    accountOperation.setOperationDate(new Date());
                    accountOperation.setAmount(Math.random()*12000);
                    accountOperation.setType(Math.random()>0.5? OperationType.DEBIT: OperationType.CREDIT);
                    accountOperation.setBankAccount(acc);
                    accountOperationRepository.save(accountOperation);
                }

            });
        };

    }

}