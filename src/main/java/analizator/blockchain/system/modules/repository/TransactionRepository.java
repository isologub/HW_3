package analizator.blockchain.system.modules.repository;

import analizator.blockchain.system.modules.repository.model.EthTransactionData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<EthTransactionData, Long> {

    @Query("SELECT e FROM EthTransactionData e WHERE e.timestamp >= :startOfDay AND e.timestamp <= :endOfDay")
    List<EthTransactionData> findEntitiesForCurrentDay(@Param("startOfDay") Long startOfDay, @Param("endOfDay") Long endOfDay);
}

