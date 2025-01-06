package com.ist.main.service;

import com.ist.main.dto.AccountRequestDto;
import com.ist.main.dto.AccountResponseDto;
import com.ist.main.entity.Account;
import com.ist.main.entity.User;
import com.ist.main.exception.BusinessException;
import com.ist.main.repository.IAccountRepository;
import com.ist.main.repository.IUserRepository;
import com.ist.main.service.interfaces.IAccountService;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService implements IAccountService {

    private final IAccountRepository accountRepository;
    private final IUserRepository userRepository;

    @Override
    public Account createAccount(AccountRequestDto accountRequestDto) throws BusinessException {
        User user = userRepository
                .findById(accountRequestDto.getUserId())
                .orElseThrow(() ->
                        new BusinessException(String.format("'%s' user_id not found", accountRequestDto.getUserId())));
        Account account = new Account();
        account.setUser(user);
        account.setBalance(accountRequestDto.getInitialBalance());
        return accountRepository.save(account);
    }

    @Override
    public List<Account> findAllByUserId(String userId) {
        return accountRepository.findByUserId(userId);
    }

    @Override
    public List<AccountResponseDto> findAccountByBalanceGreaterThan(BigDecimal balance) {
        return accountRepository.findAccountByAmountGreaterThan(balance);
    }
}
