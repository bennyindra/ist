package com.ist.main.controller;

import com.ist.main.dto.SignupRequestDto;
import com.ist.main.exception.ApplicationException;
import com.ist.main.exception.BusinessException;
import com.ist.main.service.interfaces.IUserLoginService;
import java.util.Map;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Create User Login Controller", description = "API for Create User Login")
public class UserLoginController {

    private final IUserLoginService userLoginService;

    @PostMapping("/register")
    @Operation(summary = "Register User Login", description = "register new user login, this API only able to use by User Login with ROLE_ADMIN role")
    public ResponseEntity<Map<String, String>> registerUser(@RequestBody SignupRequestDto signUpRequest)
            throws BusinessException, ApplicationException {
        userLoginService.registerLogin(signUpRequest);
        return ResponseEntity.ok(Map.of("message", "User registered successfully!"));
    }
}
