package com.ist.main.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ist.main.entity.listener.AccountEntityListener;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "account")
@Setter
@Getter
@NoArgsConstructor
@SQLRestriction("deleted = false")
@SQLDelete(sql = "UPDATE account SET deleted = true WHERE id = ? and version = ?")
@JsonIgnoreProperties({"created_by", "updated_at", "version", "user"})
@EntityListeners(AccountEntityListener.class)
public class Account extends BaseEntity {

    @Column(name = "balance")
    private BigDecimal balance;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @Fetch(FetchMode.SELECT)
    private User user;
}
