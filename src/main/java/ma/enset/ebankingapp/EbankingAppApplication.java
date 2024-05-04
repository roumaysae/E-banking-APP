package ma.enset.ebankingapp;

import ma.enset.ebankingapp.entities.*;
import ma.enset.ebankingapp.enums.AccountStatus;
import ma.enset.ebankingapp.enums.OperationType;
import ma.enset.ebankingapp.exceptions.BalanceNotSufficient;
import ma.enset.ebankingapp.exceptions.BankAccountNotFoundException;
import ma.enset.ebankingapp.repositories.BankingAccountRepository;
import ma.enset.ebankingapp.repositories.CustomerRepository;
import ma.enset.ebankingapp.repositories.OperationRepository;
import ma.enset.ebankingapp.services.BankAccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.Random;
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
            Stream.of("anouar", "meryem", "mohamed").forEach(name -> {
                Customer customer = new Customer();
                customer.setName(name);
                customer.setEmail(name + "@gmail.com");
                bankAccountService.saveCustomer(customer);
            });
            bankAccountService.listCustomers().forEach(customer -> {
                bankAccountService.saveCurrentBankAccount(Math.random() * 1200, 12000, customer.getId());
                bankAccountService.saveSavingBankAccount(Math.random() * 1000, 5.5, customer.getId());
                bankAccountService.bankAcountsList().forEach(account ->{
                    for (int i = 0; i <10 ; i++) {
                        try {
                            bankAccountService.credit(account.getId(),Math.random()*100000,"Credit vers this account");
                            bankAccountService.debit(account.getId(),1000+Math.random()*9000,"debit from this account");
                        } catch (BankAccountNotFoundException | BalanceNotSufficient e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
            });
};
    }
   // @Bean
    CommandLineRunner start(CustomerRepository customerRepository,
                            BankingAccountRepository bankAccountRepository,
                            OperationRepository operationRepository){
        return args -> {
            Stream.of("anouar","meryem","mohamed").forEach(name ->{
                Customer customer = new Customer();
                customer.setName(name);
                customer.setEmail(name+"@gmail.com");
                customerRepository.save(customer);
            });

customerRepository.findAll().forEach(customer -> {
    CurrentAccount currentAccount = new CurrentAccount();
    currentAccount.setId(UUID.randomUUID().toString());
    currentAccount.setBalance(Math.random()*1200);
    currentAccount.setCreatedDate(new Date());
    currentAccount.setStatus(AccountStatus.CREATED);
    currentAccount.setCustomer(customer);
    currentAccount.setOverdraft(12000);
    bankAccountRepository.save(currentAccount);

//pour chaque client on save un compte courant et un compte epargne
    SavingAccount savingAccount = new SavingAccount();
    savingAccount.setId(UUID.randomUUID().toString());
    savingAccount.setBalance(Math.random()*1200);
    savingAccount.setCreatedDate(new Date());
    savingAccount.setStatus(AccountStatus.CREATED);
    savingAccount.setCustomer(customer);
    savingAccount.setInterestRate(5.5);
    bankAccountRepository.save(savingAccount);
});
//
//bankAccountRepository.findAll().forEach(bankAccount -> {
//    for (int i = 0; i < 10; i++) {
//        AccountOperation accountOperation = new AccountOperation();
//        accountOperation.setOperationDate(new Date());
//        accountOperation.setType(Math.random()>0.5?OperationType.DEBIT:OperationType.CREDIT);
//        accountOperation.setAmount(Math.random()*1200);
//        accountOperation.setBankAccount(bankAccount);
//        operationRepository.save(accountOperation);
//    }
//});

//            bankAccount bankaccount =
//                    bankAccountRepository.findById("2032e19b-e77c-428a-90f1-3aad87be141c").get();
//            if (bankaccount != null) {
//                System.out.println("*******************bank account details");
//                System.out.println(bankaccount.getId());
//                System.out.println(bankaccount.getBalance());
//                System.out.println(bankaccount.getStatus());
//                System.out.println(bankaccount.getCreatedDate());
//                System.out.println(bankaccount.getCustomer().getName());
//                System.out.println(bankaccount.getClass().getSimpleName());//le nom de la classe de ce compte soit current soit saving
//                if (bankaccount instanceof CurrentAccount) {
//                    System.out.println("Over draft of currentAccount" + ((CurrentAccount) bankaccount).getOverdraft());
//                } else if (bankaccount instanceof SavingAccount) {
//                    System.out.println("Interest rate of SavingAccount" + ((SavingAccount) bankaccount).getInterestRate());
//                }
//                bankaccount.getOperationList().forEach(ops -> {
//                    System.out.println("operations list ");
//                    System.out.println(ops.getType());
//                    System.out.println(ops.getAmount());
//                    System.out.println(ops.getOperationDate());
//                });
//

        };
    }

}
