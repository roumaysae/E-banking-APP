package ma.enset.ebankingapp.dtos;

import lombok.Data;
import ma.enset.ebankingapp.enums.AccountStatus;

import java.util.Date;


@Data
public class CurrentbankAccountDTO extends BankAccountDTO {
    private String id;
    private double balance;
    private Date createdDate;
    private AccountStatus status;
    private CustomerDTO customerDTO;//cle etrangere  refere à un cle primaire de la class Customer
    private double overDraft;
}
