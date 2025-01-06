package com.ist.main.dto;

import com.ist.main.entity.Transaction;
import com.ist.main.enums.TransactionType;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class TransactionResponseDto {

    public TransactionResponseDto() {}

    public TransactionResponseDto(Transaction transaction) {
        this.id = transaction.getId();
        this.accountId = transaction.getAccount().getId();
        this.type = transaction.getType();
        this.amount = transaction.getAmount();
        this.timestamp = transaction.getCreatedAt();
    }

    private String id;
    private String accountId;
    private TransactionType type;
    private BigDecimal amount;
    private LocalDateTime timestamp;
}
