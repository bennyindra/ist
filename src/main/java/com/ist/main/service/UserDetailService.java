package com.ist.main.service;

import com.ist.main.dto.UserDetailDto;
import com.ist.main.entity.UserLogin;
import com.ist.main.repository.IUserLoginRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserDetailService implements UserDetailsService {
    private final IUserLoginRepository userLoginRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        UserLogin userLogin = userLoginRepository.findByUsername(username).orElseThrow();
        return UserDetailDto.build(userLogin);
    }
}
