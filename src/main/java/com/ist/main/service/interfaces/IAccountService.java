package com.ist.main.service.interfaces;

import com.ist.main.dto.AccountRequestDto;
import com.ist.main.dto.AccountResponseDto;
import com.ist.main.entity.Account;
import com.ist.main.exception.BusinessException;
import java.math.BigDecimal;
import java.util.List;

public interface IAccountService {

    Account createAccount(AccountRequestDto accountRequestDto) throws BusinessException;

    List<Account> findAllByUserId(String userId);

    List<AccountResponseDto> findAccountByBalanceGreaterThan(BigDecimal amount);
}
