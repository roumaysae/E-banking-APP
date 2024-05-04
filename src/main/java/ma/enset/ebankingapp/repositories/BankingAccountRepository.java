package ma.enset.ebankingapp.repositories;

import ma.enset.ebankingapp.entities.bankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankingAccountRepository extends JpaRepository<bankAccount, String> {

}
