package com.ist.main.entity.listener;

import com.ist.main.entity.UserLogin;
import com.ist.main.utils.SecurityUtils;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.time.LocalDateTime;
import java.util.UUID;
import org.apache.commons.lang3.StringUtils;

public class UserLoginEntityListener {
    @PrePersist
    void onPrePersist(UserLogin entity) {
        if (StringUtils.isBlank(entity.getId())) {
            entity.setId(UUID.randomUUID().toString());
        }
        if (entity.getCreatedAt() == null) {
            entity.setCreatedAt(LocalDateTime.now());
        }
        if (StringUtils.isBlank(entity.getCreatedBy())) {
            String createdBy =
                    StringUtils.isNoneBlank(SecurityUtils.getUsername()) ? SecurityUtils.getUsername() : "SYSTEM";
            entity.setCreatedBy(createdBy);
        }
    }

    @PreUpdate
    protected void onPreUpdate(UserLogin entity) {
        if (entity.getUpdatedAt() == null) {
            entity.setUpdatedAt(LocalDateTime.now());
        }
    }
}
