package com.ist.main.repository;

import com.ist.main.dto.ReportResponseDto;
import com.ist.main.enums.TransactionType;
import java.time.LocalDate;

public interface ITransactionReadRepository {
    ReportResponseDto getReport(TransactionType type, LocalDate startDate, LocalDate endDate);
}
