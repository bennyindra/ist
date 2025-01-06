package com.ist.main.repository;

import com.ist.main.dto.TransactionReportResponseDto;
import com.ist.main.dto.TransactionResponseDto;
import com.ist.main.entity.Transaction;
import com.ist.main.enums.TransactionType;
import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ITransactionRepository extends JpaRepository<Transaction, String> {

    @Query("select new com.ist.main.dto.TransactionResponseDto(t) from Transaction t where account.id = :accountId")
    Page<TransactionResponseDto> findByAccountId(@Param("accountId") String accountId, Pageable pageable);

    @Query(
            value = "SELECT t.type as type, count(t.id) as totalTransaction, sum(t.amount) as totalAmount "
                    + "FROM Transaction t "
                    + "WHERE t.type = :type AND createdAt >= :startDate AND createdAt <= :endDate "
                    + "GROUP BY t.type")
    TransactionReportResponseDto findSummary(
            @Param("type") TransactionType type,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);
}
