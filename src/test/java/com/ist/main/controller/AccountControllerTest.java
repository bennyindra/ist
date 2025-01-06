package com.ist.main.controller;

import static org.junit.jupiter.api.Assertions.*;

import com.ist.main.dto.AccountRequestDto;
import com.ist.main.dto.AccountResponseDto;
import com.ist.main.entity.Account;
import com.ist.main.entity.User;
import com.ist.main.exception.BusinessException;
import com.ist.main.service.interfaces.IAccountService;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

class AccountControllerTest {

    private final IAccountService accountService = Mockito.mock(IAccountService.class);
    private final AccountController accountController = new AccountController(accountService);

    public static final String DUMMY_USER_ID = "dummy-user-id";

    @Test
    void createAccount() throws BusinessException {
        Mockito.when(accountService.createAccount(ArgumentMatchers.any(AccountRequestDto.class)))
                .thenReturn(createDummyAccount());
        ResponseEntity<AccountResponseDto> response = accountController.createAccount(new AccountRequestDto());
        assertNotNull(response.getBody());
        assertEquals(BigDecimal.ONE, response.getBody().getBalance());
        assertEquals(DUMMY_USER_ID, response.getBody().getUserId());
    }

    @Test
    void findByUserId() {
        Mockito.when(accountService.findAllByUserId(DUMMY_USER_ID)).thenReturn(List.of(createDummyAccount()));
        ResponseEntity<List<AccountResponseDto>> response = accountController.findByUserId(DUMMY_USER_ID);
        assertNotNull(response.getBody());
        assertEquals(BigDecimal.ONE, response.getBody().get(0).getBalance());
        assertEquals(DUMMY_USER_ID, response.getBody().get(0).getUserId());
    }

    @Test
    void findAccountByBalanceGreaterThan() {
        Mockito.when(accountService.findAccountByBalanceGreaterThan(ArgumentMatchers.any(BigDecimal.class)))
                .thenReturn(List.of(new AccountResponseDto(createDummyAccount())));
        ResponseEntity<List<AccountResponseDto>> response =
                accountController.findAccountByBalanceGreaterThan(BigDecimal.ONE);
        assertNotNull(response.getBody());
        assertEquals(BigDecimal.ONE, response.getBody().get(0).getBalance());
        assertEquals(DUMMY_USER_ID, response.getBody().get(0).getUserId());
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
