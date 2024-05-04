package ma.enset.ebankingapp.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.enset.ebankingapp.enums.AccountStatus;

import java.util.Date;
import java.util.List;
@Entity
@Data @AllArgsConstructor @NoArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) //chaque classe qui herite de cette bankAccount class,
// ses attributs vont etre dans la meme table bankaccount
@DiscriminatorColumn(name = "TYPE",length = 4)//la colonne qui faire la difference entre les deux accounts

public class bankAccount {
    @Id
    private String id;
    private double balance;
    private Date createdDate;

    @Enumerated(EnumType.STRING)
    private AccountStatus status;

    @ManyToOne
    private Customer customer;//cle etrangere  refere à un cle primaire de la class Customer
    @OneToMany(mappedBy = "bankAccount",fetch = FetchType.EAGER)
    private List<AccountOperation> operationList;

}
