package com.ist.main.repository;

import com.ist.main.entity.UserLogin;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserLoginRepository extends JpaRepository<UserLogin, String> {
    Optional<UserLogin> findByUsername(String username);

    Boolean existsByUsername(String username);
}
