package ma.enset.ebankingapp.repositories;

import ma.enset.ebankingapp.dtos.AccountHistoryDTO;
import ma.enset.ebankingapp.entities.AccountOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OperationRepository extends JpaRepository<AccountOperation, Long> {
        List<AccountOperation> findByBankAccountId(String accountid);
        Page<AccountOperation> findByBankAccountIdOrderByOperationDateDesc(String bankAccountId, Pageable pageable);
}
