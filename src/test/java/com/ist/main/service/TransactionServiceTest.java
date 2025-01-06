package com.ist.main.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.ist.main.dto.ReportResponseDto;
import com.ist.main.dto.TransactionRequestDto;
import com.ist.main.dto.TransactionResponseDto;
import com.ist.main.entity.Account;
import com.ist.main.entity.Transaction;
import com.ist.main.entity.User;
import com.ist.main.enums.TransactionType;
import com.ist.main.exception.BusinessException;
import com.ist.main.repository.IAccountRepository;
import com.ist.main.repository.ITransactionReadRepository;
import com.ist.main.repository.ITransactionRepository;
import com.ist.main.service.interfaces.ITransactionService;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

class TransactionServiceTest {
    private final IAccountRepository accountRepository = Mockito.mock(IAccountRepository.class);
    private final ITransactionRepository transactionRepository = Mockito.mock(ITransactionRepository.class);
    private final ITransactionReadRepository transactionReadRepository = Mockito.mock(ITransactionReadRepository.class);
    private final ITransactionService transactionService =
            new TransactionService(accountRepository, transactionRepository, transactionReadRepository);

    public static final String DUMMY_USER_ID = "dummy-user-id";
    public static final String DUMMY_USER_ACCOUNT_ID = "dummy-user-account-id";
    public static final String DUMMY_USER_ACCOUNT_TRANSACTION_ID = "dummy-user-account-transaction-id";

    @ParameterizedTest
    @EnumSource(TransactionType.class)
    void postTransaction(TransactionType type) throws BusinessException {
        TransactionRequestDto transactionRequestDto = new TransactionRequestDto();
        transactionRequestDto.setType(type);
        transactionRequestDto.setAmount(BigDecimal.ONE);
        transactionRequestDto.setAccountId(DUMMY_USER_ACCOUNT_ID);

        Mockito.when(accountRepository.findById(ArgumentMatchers.eq(DUMMY_USER_ACCOUNT_ID)))
                .thenReturn(Optional.of(createDummyAccount()));
        Transaction transaction = transactionService.postTransaction(transactionRequestDto);
        assertNotNull(transaction);
        assertEquals(type, transaction.getType());
        assertEquals(BigDecimal.ONE, transaction.getAmount());
        assertEquals(DUMMY_USER_ACCOUNT_ID, transaction.getAccount().getId());
    }

    @Test
    void postTransactionAccountNotExists() {
        TransactionRequestDto transactionRequestDto = new TransactionRequestDto();
        transactionRequestDto.setType(TransactionType.DEBIT);
        transactionRequestDto.setAmount(BigDecimal.ONE);
        transactionRequestDto.setAccountId(DUMMY_USER_ACCOUNT_ID);
        Account account = createDummyAccount();
        account.setBalance(BigDecimal.ZERO);
        Mockito.when(accountRepository.findById(ArgumentMatchers.eq(DUMMY_USER_ACCOUNT_ID)))
                .thenReturn(Optional.empty());
        BusinessException businessException =
                assertThrows(BusinessException.class, () -> transactionService.postTransaction(transactionRequestDto));
        assertEquals(
                String.format("'%s' account_id not found", DUMMY_USER_ACCOUNT_ID), businessException.getErrorMessage());
    }

    @Test
    void postTransactionAccountBalanceNotEnough() {
        TransactionRequestDto transactionRequestDto = new TransactionRequestDto();
        transactionRequestDto.setType(TransactionType.DEBIT);
        transactionRequestDto.setAmount(BigDecimal.ONE);
        transactionRequestDto.setAccountId(DUMMY_USER_ACCOUNT_ID);
        Account account = createDummyAccount();
        account.setBalance(BigDecimal.ZERO);

        Mockito.when(accountRepository.findById(ArgumentMatchers.eq(DUMMY_USER_ACCOUNT_ID)))
                .thenReturn(Optional.of(account));
        BusinessException businessException =
                assertThrows(BusinessException.class, () -> transactionService.postTransaction(transactionRequestDto));
        assertEquals("balance is not enough", businessException.getErrorMessage());
    }

    @Test
    void getTransactionHistoryByAccountId() {

        Pageable pageRequest = Mockito.mock(PageRequest.class);
        Page<TransactionResponseDto> transactionResponseDto =
                new PageImpl<>(List.of(new TransactionResponseDto(createDummyTransaction())), pageRequest, 1);
        Mockito.when(transactionRepository.findByAccountId(
                        ArgumentMatchers.eq(DUMMY_USER_ACCOUNT_ID), ArgumentMatchers.any(Pageable.class)))
                .thenReturn(transactionResponseDto);
        Page<TransactionResponseDto> responseDtoPage =
                transactionService.getTransactionHistoryByAccountId(DUMMY_USER_ACCOUNT_ID, pageRequest);
        assertNotNull(responseDtoPage);
        assertNotNull(responseDtoPage.getContent());
        assertEquals(DUMMY_USER_ACCOUNT_ID, responseDtoPage.getContent().get(0).getAccountId());
        assertEquals(
                DUMMY_USER_ACCOUNT_TRANSACTION_ID,
                responseDtoPage.getContent().get(0).getId());
        assertEquals(BigDecimal.ONE, responseDtoPage.getContent().get(0).getAmount());
    }

    @ParameterizedTest
    @EnumSource(TransactionType.class)
    void summary(TransactionType type) {
        Mockito.when(transactionReadRepository.getReport(
                        ArgumentMatchers.eq(type),
                        ArgumentMatchers.any(LocalDate.class),
                        ArgumentMatchers.any(LocalDate.class)))
                .thenReturn(new ReportResponseDto(type, 10L, BigDecimal.TEN));
        ReportResponseDto responseDto =
                transactionService.summary(type, LocalDate.now().minusDays(1), LocalDate.now());
        assertNotNull(responseDto);
        assertEquals(type, responseDto.getType());
        assertEquals(10L, responseDto.getTotalTransaction());
        assertEquals(BigDecimal.TEN, responseDto.getTotalAmount());
    }

    private User createDummyUser() {
        User user = new User();
        user.setId(DUMMY_USER_ID);
        user.setName("dummy user");
        user.setEmail("dummyuser@gmail.com");
        return user;
    }

    private Account createDummyAccount() {
        Account account = new Account();
        account.setId(DUMMY_USER_ACCOUNT_ID);
        account.setUser(createDummyUser());
        account.setBalance(BigDecimal.TEN);
        return account;
    }

    private Transaction createDummyTransaction() {
        Transaction transaction = new Transaction();
        transaction.setAccount(createDummyAccount());
        transaction.setType(TransactionType.DEBIT);
        transaction.setAmount(BigDecimal.ONE);
        transaction.setId(DUMMY_USER_ACCOUNT_TRANSACTION_ID);
        return transaction;
    }
}
