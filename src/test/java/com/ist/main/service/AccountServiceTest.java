package com.ist.main.service;

import static org.junit.jupiter.api.Assertions.*;

import com.ist.main.dto.AccountRequestDto;
import com.ist.main.dto.AccountResponseDto;
import com.ist.main.entity.Account;
import com.ist.main.entity.User;
import com.ist.main.exception.BusinessException;
import com.ist.main.repository.IAccountRepository;
import com.ist.main.repository.IUserRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

class AccountServiceTest {

    private final IAccountRepository accountRepository = Mockito.mock(IAccountRepository.class);
    private final IUserRepository userRepository = Mockito.mock(IUserRepository.class);
    private final AccountService accountService = new AccountService(accountRepository, userRepository);

    public static final String DUMMY_USER_ID = "dummy-user-id";

    @Test
    void createAccount() throws BusinessException {
        AccountRequestDto accountRequestDto = new AccountRequestDto();
        accountRequestDto.setInitialBalance(BigDecimal.ZERO);
        accountRequestDto.setUserId(DUMMY_USER_ID);
        Mockito.when(userRepository.findById(ArgumentMatchers.eq(DUMMY_USER_ID)))
                .thenReturn(Optional.of(createDummyUser()));
        Mockito.when(accountRepository.save(ArgumentMatchers.any(Account.class)))
                .thenReturn(createDummyAccount());
        Account account = accountService.createAccount(accountRequestDto);
        assertEquals(BigDecimal.ONE, account.getBalance());
        assertEquals(DUMMY_USER_ID, account.getUser().getId());
        Mockito.verify(userRepository, Mockito.times(1)).findById(ArgumentMatchers.eq(DUMMY_USER_ID));
        Mockito.verify(accountRepository, Mockito.times(1)).save(ArgumentMatchers.any(Account.class));
    }

    @Test
    void findAllByUserId() {
        Mockito.when(accountService.findAllByUserId(ArgumentMatchers.eq(DUMMY_USER_ID)))
                .thenReturn(List.of(createDummyAccount()));
        List<Account> accounts = accountService.findAllByUserId(DUMMY_USER_ID);
        assertFalse(accounts.isEmpty());
        assertEquals(BigDecimal.ONE, accounts.get(0).getBalance());
        assertEquals(DUMMY_USER_ID, accounts.get(0).getUser().getId());
        Mockito.verify(accountRepository, Mockito.times(1)).findByUserId(ArgumentMatchers.eq(DUMMY_USER_ID));
    }

    @Test
    void findAccountByBalanceGreaterThan() {
        Mockito.when(accountService.findAccountByBalanceGreaterThan(BigDecimal.ONE))
                .thenReturn(List.of(new AccountResponseDto(createDummyAccount())));
        List<AccountResponseDto> accountResponseDtoList =
                accountService.findAccountByBalanceGreaterThan(BigDecimal.ONE);
        assertFalse(accountResponseDtoList.isEmpty());
        assertEquals(BigDecimal.ONE, accountResponseDtoList.get(0).getBalance());
        assertEquals(DUMMY_USER_ID, accountResponseDtoList.get(0).getUserId());
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
        account.setUser(createDummyUser());
        account.setBalance(BigDecimal.ONE);
        return account;
    }
}
