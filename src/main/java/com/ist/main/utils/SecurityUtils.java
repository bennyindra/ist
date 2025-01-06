package com.ist.main.utils;

import com.ist.main.dto.UserDetailDto;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;

public class SecurityUtils {
    public SecurityUtils() {}

    public static UserDetailDto getUserDetails() throws ResponseStatusException {
        return getUserDetails(SecurityContextHolder.getContext().getAuthentication());
    }

    public static UserDetailDto getUserDetails(Authentication authentication) throws ResponseStatusException {
        if (authentication != null && authentication.getPrincipal() instanceof UserDetailDto) {
            return (UserDetailDto) authentication.getPrincipal();
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

    public static Optional<UserDetailDto> getUserDetailsIfAvailable() {
        return getUserDetailsIfAvailable(SecurityContextHolder.getContext().getAuthentication());
    }

    public static Optional<UserDetailDto> getUserDetailsIfAvailable(Authentication authentication) {
        return authentication != null && authentication.getPrincipal() instanceof UserDetailDto
                ? Optional.of((UserDetailDto) authentication.getPrincipal())
                : Optional.empty();
    }

    public static String getUsername() throws ResponseStatusException {
        try {
            return getUserDetails().getUsername();
        } catch (ResponseStatusException e) {
            return null;
        }
    }
}
