package ma.enset.ebankingapp.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Entity
@Data @AllArgsConstructor @NoArgsConstructor
public class Customer {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    @OneToMany(mappedBy = "customer")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) //il est dure à chaque fois il faut revenir ici au code des entités et changer le json property donc la bonne pratique est d'utiliser DTOs au lieu que entities
    private List<bankAccount> bankAccounts; //se refere au classe bankaccount (un customer peut avoir une ou plusieurs bankaccounts)

}

