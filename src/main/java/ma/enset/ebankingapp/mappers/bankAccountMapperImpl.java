package ma.enset.ebankingapp.mappers;

import ch.qos.logback.core.joran.util.beans.BeanUtil;
import ma.enset.ebankingapp.dtos.AccountOperationDTO;
import ma.enset.ebankingapp.dtos.CurrentbankAccountDTO;
import ma.enset.ebankingapp.dtos.CustomerDTO;
import ma.enset.ebankingapp.dtos.SavingbankAccountDTO;
import ma.enset.ebankingapp.entities.AccountOperation;
import ma.enset.ebankingapp.entities.CurrentAccount;
import ma.enset.ebankingapp.entities.Customer;
import ma.enset.ebankingapp.entities.SavingAccount;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
public class bankAccountMapperImpl {
    public CustomerDTO toCustomerDTO(Customer customer){
            CustomerDTO customerDTO=new CustomerDTO();
         customerDTO.setId(customer.getId());
        customerDTO.setName(customer.getName());
        customerDTO.setEmail(customer.getEmail());
//        BeanUtils.copyProperties(customer,customerDTO);
        return  customerDTO;
    }
    public Customer toCustomer(CustomerDTO customerDTO){
        Customer customer=new Customer();
        BeanUtils.copyProperties(customerDTO,customer);
        return  customer;
    }

    public SavingbankAccountDTO toSavingBankAccountDTO(SavingAccount savingAccount){
        SavingbankAccountDTO savingBankAccountDTO=new SavingbankAccountDTO();
        BeanUtils.copyProperties(savingAccount,savingBankAccountDTO);
        savingBankAccountDTO.setCustomerDTO(toCustomerDTO(savingAccount.getCustomer()));
        savingBankAccountDTO.setType(savingAccount.getClass().getSimpleName());
        return savingBankAccountDTO;
    }

    public SavingAccount toSavingBankAccount(SavingbankAccountDTO savingBankAccountDTO){
        SavingAccount savingAccount=new SavingAccount();
        BeanUtils.copyProperties(savingBankAccountDTO,savingAccount);
        savingAccount.setCustomer(toCustomer(savingBankAccountDTO.getCustomerDTO()));
        return savingAccount;
    }

    public CurrentbankAccountDTO toCurrentBanKAccountDTO(CurrentAccount currentAccount){
        CurrentbankAccountDTO currentBankAccountDTO=new CurrentbankAccountDTO();
        BeanUtils.copyProperties(currentAccount,currentBankAccountDTO);
        currentBankAccountDTO.setCustomerDTO(toCustomerDTO(currentAccount.getCustomer()));
        currentBankAccountDTO.setType(currentAccount.getClass().getSimpleName());
        return currentBankAccountDTO;
    }

    public CurrentAccount toCurrentBankAccount(CurrentbankAccountDTO currentBankAccountDTO){
        CurrentAccount currentAccount=new CurrentAccount();
        BeanUtils.copyProperties(currentBankAccountDTO,currentAccount);
        currentAccount.setCustomer(toCustomer(currentBankAccountDTO.getCustomerDTO()));
        return currentAccount;
    }

    public AccountOperationDTO toAccountOperationDTO(AccountOperation accountOperation){
        AccountOperationDTO accountOperationDTO=new AccountOperationDTO();
        BeanUtils.copyProperties(accountOperation,accountOperationDTO);
        return accountOperationDTO;
    }

}