package com.ist.main.controller;

import com.ist.main.dto.AccountRequestDto;
import com.ist.main.dto.AccountResponseDto;
import com.ist.main.exception.BusinessException;
import com.ist.main.service.interfaces.IAccountService;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
public class AccountController {

    private final IAccountService accountService;

    @PostMapping
    public ResponseEntity<AccountResponseDto> createAccount(@RequestBody AccountRequestDto accountRequestDto)
            throws BusinessException {
        return ResponseEntity.ok(new AccountResponseDto(accountService.createAccount(accountRequestDto)));
    }

    @GetMapping
    public ResponseEntity<List<AccountResponseDto>> findByUserId(@RequestParam(name = "user_id") String userId) {
        return ResponseEntity.ok(accountService.findAllByUserId(userId).stream()
                .map(AccountResponseDto::new)
                .toList());
    }

    @GetMapping("/balance/gt")
    public ResponseEntity<List<AccountResponseDto>> findAccountByBalanceGreaterThan(@RequestParam BigDecimal balance) {
        return ResponseEntity.ok(accountService.findAccountByBalanceGreaterThan(balance));
    }
}
