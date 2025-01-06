package com.ist.main.service.interfaces;

import com.ist.main.dto.ReportResponseDto;
import com.ist.main.dto.TransactionRequestDto;
import com.ist.main.dto.TransactionResponseDto;
import com.ist.main.entity.Transaction;
import com.ist.main.enums.TransactionType;
import com.ist.main.exception.BusinessException;
import java.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ITransactionService {

    Transaction postTransaction(TransactionRequestDto transactionRequestDto) throws BusinessException;

    Page<TransactionResponseDto> getTransactionHistoryByAccountId(String accountId, Pageable pageable);

    ReportResponseDto summary(TransactionType type, LocalDate startDate, LocalDate endDate);
}
