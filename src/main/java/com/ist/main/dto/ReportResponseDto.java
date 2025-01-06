package com.ist.main.dto;

import com.ist.main.enums.TransactionType;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class ReportResponseDto {

    public ReportResponseDto(TransactionType type, Long totalTransaction, BigDecimal totalAmount) {
        this.type = type;
        this.totalTransaction = totalTransaction;
        this.totalAmount = totalAmount;
    }

    private TransactionType type;
    private Long totalTransaction;
    private BigDecimal totalAmount;
}
