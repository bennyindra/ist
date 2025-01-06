package com.ist.main.controller;

import com.ist.main.dto.ReportResponseDto;
import com.ist.main.dto.TransactionRequestDto;
import com.ist.main.dto.TransactionResponseDto;
import com.ist.main.entity.Account;
import com.ist.main.entity.Transaction;
import com.ist.main.enums.TransactionType;
import com.ist.main.exception.BusinessException;
import com.ist.main.service.interfaces.ITransactionService;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

class TransactionControllerTest {

    public static final String DUMMY_ACCOUNT_ID = "dummy-account-id";
    public static final String DUMMY_ID = "dummy-id";
    private final ITransactionService transactionService = Mockito.mock(ITransactionService.class);

    private final TransactionController transactionController = new TransactionController(transactionService);

    @Test
    void postTransactionSucceeded() throws BusinessException {
        TransactionRequestDto transactionRequestDto = new TransactionRequestDto();
        transactionRequestDto.setAccountId(DUMMY_ACCOUNT_ID);
        transactionRequestDto.setAmount(BigDecimal.ONE);
        transactionRequestDto.setType(TransactionType.DEBIT);
        transactionRequestDto.setAccountId(DUMMY_ACCOUNT_ID);
        Transaction transaction = constructDummyTransaction();
        Mockito.when(transactionService.postTransaction(ArgumentMatchers.any(TransactionRequestDto.class)))
                .thenReturn(transaction);
        ResponseEntity<TransactionResponseDto> response =
                transactionController.postTransaction(new TransactionRequestDto());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(DUMMY_ID, response.getBody().getId());
        Assertions.assertEquals(DUMMY_ACCOUNT_ID, response.getBody().getAccountId());
    }

    @Test
    void getHistoryTransactionSucceeded() {
        TransactionResponseDto transactionResponseDto = new TransactionResponseDto();
        transactionResponseDto.setId(DUMMY_ID);
        transactionResponseDto.setAccountId(DUMMY_ACCOUNT_ID);
        Page<TransactionResponseDto> responseDtoPage = new PageImpl<>(List.of(transactionResponseDto));
        Mockito.when(transactionService.getTransactionHistoryByAccountId(
                        ArgumentMatchers.anyString(), ArgumentMatchers.any(Pageable.class)))
                .thenReturn(responseDtoPage);
        ResponseEntity<Page<TransactionResponseDto>> response =
                transactionController.getHistoryTransaction(DUMMY_ACCOUNT_ID, 10, 1);
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(1, response.getBody().getContent().size());
        Assertions.assertEquals(DUMMY_ID, response.getBody().getContent().get(0).getId());
        Assertions.assertEquals(
                DUMMY_ACCOUNT_ID, response.getBody().getContent().get(0).getAccountId());
    }

    @Test
    void summarySucceeded() {
        ReportResponseDto reportResponseDto =
                new ReportResponseDto(TransactionType.DEBIT, 100L, BigDecimal.valueOf(50000));
        Mockito.when(transactionService.summary(
                        ArgumentMatchers.eq(TransactionType.DEBIT),
                        ArgumentMatchers.any(LocalDate.class),
                        ArgumentMatchers.any(LocalDate.class)))
                .thenReturn(reportResponseDto);
        ResponseEntity<ReportResponseDto> response =
                transactionController.summary(TransactionType.DEBIT, LocalDate.now(), LocalDate.now());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(TransactionType.DEBIT, response.getBody().getType());
        Assertions.assertEquals(BigDecimal.valueOf(50000), response.getBody().getTotalAmount());
        Assertions.assertEquals(100L, response.getBody().getTotalTransaction());
    }

    private static Transaction constructDummyTransaction() {
        Transaction transaction = new Transaction();

        Account account = new Account();
        account.setId(DUMMY_ACCOUNT_ID);
        transaction.setId(DUMMY_ID);
        transaction.setAccount(account);
        return transaction;
    }
}
