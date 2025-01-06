package com.ist.main.controller;

import com.ist.main.dto.SignupRequestDto;
import com.ist.main.exception.ApplicationException;
import com.ist.main.exception.BusinessException;
import com.ist.main.service.interfaces.IUserLoginService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@PreAuthorize("hasRole('ROLE_ADMIN')")
@RestController
@RequestMapping("/api/userlogin")
@RequiredArgsConstructor
public class UserLoginController {

    private final IUserLoginService userLoginService;

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registerUser(@RequestBody SignupRequestDto signUpRequest)
            throws BusinessException, ApplicationException {
        userLoginService.registerLogin(signUpRequest);
        return ResponseEntity.ok(Map.of("message", "User registered successfully!"));
    }
}
