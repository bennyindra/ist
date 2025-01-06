package com.ist.main.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ist.main.entity.listener.TransactionEntityListener;
import com.ist.main.enums.TransactionType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "transactions")
@Setter
@Getter
@NoArgsConstructor
@SQLDelete(sql = "UPDATE transactions SET deleted = true WHERE id = ? and version = ?")
@SQLRestriction("deleted = false")
@JsonIgnoreProperties({"created_by", "updated_at", "version"})
@EntityListeners(TransactionEntityListener.class)
public class Transaction extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private TransactionType type;

    @Column(name = "amount")
    private BigDecimal amount;

    @JsonProperty("timestamp")
    @Override
    public LocalDateTime getCreatedAt() {
        return super.getCreatedAt();
    }
}
