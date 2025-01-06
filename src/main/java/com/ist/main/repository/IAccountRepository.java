package com.ist.main.repository;

import com.ist.main.dto.AccountResponseDto;
import com.ist.main.entity.Account;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IAccountRepository extends JpaRepository<Account, String> {

    List<Account> findByUserId(String userId);

    @Query("SELECT new com.ist.main.dto.AccountResponseDto(a) FROM Account a WHERE a.balance > :balance")
    List<AccountResponseDto> findAccountByAmountGreaterThan(@Param("balance") BigDecimal balance);
}
