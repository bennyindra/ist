package com.ist.main.dto;

import com.ist.main.entity.Account;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AccountResponseDto {

    public AccountResponseDto(Account account) {
        this.id = account.getId();
        this.userId = account.getUser().getId();
        this.balance = account.getBalance();
        this.createdAt = account.getCreatedAt();
    }

    private String id;
    private String userId;
    private BigDecimal balance;
    private LocalDateTime createdAt;
}
