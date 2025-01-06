package com.ist.main.entity.listener;

import com.ist.main.entity.Account;
import com.ist.main.utils.SecurityUtils;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import org.apache.commons.lang3.StringUtils;

public class AccountEntityListener {
    @PrePersist
    void onPrePersist(Account entity) {
        if (StringUtils.isBlank(entity.getId())) {
            entity.setId(UUID.randomUUID().toString());
        }
        if (entity.getCreatedAt() == null) {
            entity.setCreatedAt(LocalDateTime.now());
        }
        if (entity.getBalance() == null) {
            entity.setBalance(BigDecimal.ZERO);
        }
        if (StringUtils.isBlank(entity.getCreatedBy())) {
            String createdBy =
                    StringUtils.isNoneBlank(SecurityUtils.getUsername()) ? SecurityUtils.getUsername() : "SYSTEM";
            entity.setCreatedBy(createdBy);
        }
    }

    @PreUpdate
    protected void onPreUpdate(Account entity) {
        if (entity.getUpdatedAt() == null) {
            entity.setUpdatedAt(LocalDateTime.now());
        }
    }
}
