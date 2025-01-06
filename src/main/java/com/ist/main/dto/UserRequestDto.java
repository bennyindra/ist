package com.ist.main.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class UserRequestDto {
    @Email(message = "Invalid email")
    @NotEmpty(message = "email is empty")
    private String email;

    @NotEmpty(message = "name is empty")
    private String name;
}
