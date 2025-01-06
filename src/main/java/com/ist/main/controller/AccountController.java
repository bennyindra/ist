package com.ist.main.controller;

import com.ist.main.dto.AccountRequestDto;
import com.ist.main.dto.AccountResponseDto;
import com.ist.main.exception.BusinessException;
import com.ist.main.service.interfaces.IAccountService;
import java.math.BigDecimal;
import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Account Controller", description = "API Account Related")
public class AccountController {

    private final IAccountService accountService;

    @PostMapping
    @Operation(summary = "Create Account By UserID", description = "Create Account By UserID")
    public ResponseEntity<AccountResponseDto> createAccount(@RequestBody AccountRequestDto accountRequestDto)
            throws BusinessException {
        return ResponseEntity.ok(new AccountResponseDto(accountService.createAccount(accountRequestDto)));
    }

    @GetMapping
    @Operation(summary = "Find Accounts By UserId", description = "Find Accounts By UserId")
    public ResponseEntity<List<AccountResponseDto>> findByUserId(@RequestParam(name = "user_id") String userId) {
        return ResponseEntity.ok(accountService.findAllByUserId(userId).stream()
                .map(AccountResponseDto::new)
                .toList());
    }

    @GetMapping("/balance/gt")
    @Operation(summary = "Find Accounts that balance more than request param", description = "Find Accounts that balance more than request param")
    public ResponseEntity<List<AccountResponseDto>> findAccountByBalanceGreaterThan(@RequestParam BigDecimal balance) {
        return ResponseEntity.ok(accountService.findAccountByBalanceGreaterThan(balance));
    }
}
