package com.ist.main.repository;

import com.ist.main.dto.ReportResponseDto;
import com.ist.main.entity.Transaction;
import com.ist.main.enums.TransactionType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.springframework.stereotype.Repository;

@Repository
public class TransactionReadRepository implements ITransactionReadRepository {
    @PersistenceContext
    protected EntityManager em;

    public ReportResponseDto getReport(TransactionType type, LocalDate startDate, LocalDate endDate) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<ReportResponseDto> query = builder.createQuery(ReportResponseDto.class);
        // define root entity
        Root<Transaction> root = query.from(Transaction.class);
        // define predicate to be add
        Predicate predicate = builder.conjunction();
        // select to constructor
        query.select(builder.construct(
                ReportResponseDto.class,
                root.get("type"),
                builder.count(root.get("id")),
                builder.sum(root.get("amount"))));
        if (type != null) {
            Path<TransactionType> transactionTypePath = root.get("type");

            Predicate namePredicate = builder.equal(transactionTypePath, type);
            predicate = builder.and(predicate, namePredicate);
        }
        if (startDate != null) {
            Path<LocalDateTime> dateCreatedPath = root.get("createdAt");
            Predicate namePredicate = builder.greaterThanOrEqualTo(dateCreatedPath, startDate.atStartOfDay());
            predicate = builder.and(predicate, namePredicate);
        }

        if (endDate != null) {
            Path<LocalDateTime> dateCreatedPath = root.get("createdAt");
            Predicate namePredicate = builder.lessThanOrEqualTo(dateCreatedPath, endDate.atTime(LocalTime.MAX));
            predicate = builder.and(predicate, namePredicate);
        }

        // add predicate
        query.where(predicate);
        query.groupBy(root.get("type"));
        TypedQuery<ReportResponseDto> tq = em.createQuery(query);

        try {
            return tq.getSingleResult();
        } catch (NoResultException e) {
            return new ReportResponseDto(type, 0L, BigDecimal.ZERO);
        }
    }
}
