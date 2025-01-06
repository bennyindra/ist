package com.ist.main.controller;

import com.ist.main.dto.ReportResponseDto;
import com.ist.main.dto.TransactionRequestDto;
import com.ist.main.dto.TransactionResponseDto;
import com.ist.main.enums.TransactionType;
import com.ist.main.exception.BusinessException;
import com.ist.main.service.interfaces.ITransactionService;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transaction")
@RequiredArgsConstructor
public class TransactionController {

    private final ITransactionService transactionService;

    @PostMapping
    public ResponseEntity<TransactionResponseDto> postTransaction(@RequestBody TransactionRequestDto dto)
            throws BusinessException {
        return ResponseEntity.ok(new TransactionResponseDto(transactionService.postTransaction(dto)));
    }

    @GetMapping
    public ResponseEntity<Page<TransactionResponseDto>> getHistoryTransaction(
            @RequestParam(name = "account_id") String accountId,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "1") int page) {
        return ResponseEntity.ok(transactionService.getTransactionHistoryByAccountId(
                accountId, PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"))));
    }

    @GetMapping("/report")
    public ResponseEntity<ReportResponseDto> summary(
            @RequestParam(name = "type") TransactionType type,
            @RequestParam(name = "start_date", required = false)
                    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = "dd-MM-yyyy")
                    LocalDate startDate,
            @RequestParam(name = "end_date", required = false)
                    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = "dd-MM-yyyy")
                    LocalDate endDate) {
        return ResponseEntity.ok(transactionService.summary(type, startDate, endDate));
    }
}
