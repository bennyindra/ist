package com.ist.main.service;

import com.ist.main.dto.ReportResponseDto;
import com.ist.main.dto.TransactionRequestDto;
import com.ist.main.dto.TransactionResponseDto;
import com.ist.main.entity.Account;
import com.ist.main.entity.Transaction;
import com.ist.main.enums.TransactionType;
import com.ist.main.exception.BusinessException;
import com.ist.main.repository.IAccountRepository;
import com.ist.main.repository.ITransactionReadRepository;
import com.ist.main.repository.ITransactionRepository;
import com.ist.main.service.interfaces.ITransactionService;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionService implements ITransactionService {

    private final IAccountRepository accountRepository;
    private final ITransactionRepository transactionRepository;
    private final ITransactionReadRepository transactionReadRepository;

    @Override
    @Transactional
    public Transaction postTransaction(TransactionRequestDto transactionRequestDto) throws BusinessException {
        Account account = accountRepository
                .findById(transactionRequestDto.getAccountId())
                .orElseThrow(() -> new BusinessException(
                        String.format("'%s' account_id not found", transactionRequestDto.getAccountId())));

        if (transactionRequestDto.getType() == TransactionType.DEBIT) {
            if (transactionRequestDto.getAmount().compareTo(account.getBalance()) > 0) {
                throw new BusinessException("balance is not enough");
            }
            account.setBalance(account.getBalance().subtract(transactionRequestDto.getAmount()));
        } else if (transactionRequestDto.getType() == TransactionType.CREDIT) {
            account.setBalance(account.getBalance().add(transactionRequestDto.getAmount()));
        }
        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setType(transactionRequestDto.getType());
        transaction.setAmount(transactionRequestDto.getAmount());
        transactionRepository.save(transaction);
        accountRepository.save(account);
        return transaction;
    }

    public Page<TransactionResponseDto> getTransactionHistoryByAccountId(String accountId, Pageable pageable) {
        return transactionRepository.findByAccountId(accountId, pageable);
    }

    @Override
    public ReportResponseDto summary(TransactionType type, LocalDate startDate, LocalDate endDate) {
        return transactionReadRepository.getReport(type, startDate, endDate);
    }
}
