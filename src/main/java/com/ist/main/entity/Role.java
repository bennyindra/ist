package com.ist.main.entity;

import com.ist.main.enums.RoleEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Entity
@Table(name = "roles")
@Data
public class Role {
    @Id
    @Column(name = "ID", length = 50)
    private String id;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private RoleEnum name;

    @PrePersist
    void onPrePersist() {
        if (StringUtils.isEmpty(this.getId())) {
            this.setId(UUID.randomUUID().toString());
        }
    }
}
