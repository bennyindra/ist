package com.ist.main.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ist.main.entity.listener.UserEntityListener;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@SQLRestriction("deleted = false")
@SQLDelete(sql = "UPDATE users SET deleted = true WHERE id = ? and version = ?")
@JsonIgnoreProperties({"created_by", "updated_at", "version"})
@EntityListeners(UserEntityListener.class)
public class User extends BaseEntity {

    @Column(name = "name")
    private String name;

    // unique, email validation
    @Column(name = "email", unique = true)
    private String email;
}
