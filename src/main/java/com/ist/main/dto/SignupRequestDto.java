package com.ist.main.dto;

import java.util.Set;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SignupRequestDto {
    private String username;
    private String password;
    private Set<String> role;
}
