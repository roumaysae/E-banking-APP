package ma.enset.ebankingapp.repositories;

import ma.enset.ebankingapp.entities.AccountOperation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OperationRepository extends JpaRepository<AccountOperation, Long> {

}
