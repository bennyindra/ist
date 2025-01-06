package com.ist.main.dto;

import com.ist.main.enums.TransactionType;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionRequestDto {
    private String accountId;
    private TransactionType type;
    private BigDecimal amount;
}
