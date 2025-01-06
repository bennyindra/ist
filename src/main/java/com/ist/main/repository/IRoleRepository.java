package com.ist.main.repository;

import com.ist.main.entity.Role;
import com.ist.main.enums.RoleEnum;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IRoleRepository extends JpaRepository<Role, String> {

    Optional<Role> findByName(RoleEnum roleEnum);
}
