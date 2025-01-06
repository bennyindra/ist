package com.ist.main.dto;

import java.util.Set;

import com.ist.main.entity.Role;
import com.ist.main.enums.RoleEnum;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SignupRequestDto {
    private String username;
    private String password;
    private Set<RoleEnum> role;
}
