package com.ist.main.service;

import com.ist.main.dto.SignupRequestDto;
import com.ist.main.entity.Role;
import com.ist.main.entity.UserLogin;
import com.ist.main.enums.RoleEnum;
import com.ist.main.exception.ApplicationException;
import com.ist.main.exception.BusinessException;
import com.ist.main.repository.IRoleRepository;
import com.ist.main.repository.IUserLoginRepository;
import com.ist.main.service.interfaces.IUserLoginService;
import jakarta.transaction.Transactional;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserLoginService implements IUserLoginService {

    private final IUserLoginRepository userLoginRepository;
    private final IRoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final Supplier<ApplicationException> throwsNoUserAdminError =
            () -> new ApplicationException("no ROLE_ADMIN defined!!!");
    private final Supplier<ApplicationException> throwsNoRoleUserError =
            () -> new ApplicationException("no ROLE_ADMIN defined!!!");

    @Override
    @Transactional
    public void registerLogin(SignupRequestDto requestDto) throws BusinessException, ApplicationException {
        if (userLoginRepository.existsByUsername(requestDto.getUsername())) {
            throw new BusinessException("Error: Username is already taken!");
        }

        // Create new user's account
        UserLogin user = new UserLogin();
        user.setUsername(requestDto.getUsername());
        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));

        Set<String> strRoles = requestDto.getRole();
        Set<Role> roles = new HashSet<>();
        Optional<Role> optionalRoleAsUser = roleRepository.findByName(RoleEnum.ROLE_USER);
        if (CollectionUtils.isEmpty(strRoles)) {
            optionalRoleAsUser.orElseThrow(throwsNoRoleUserError);
        } else {
            for (String roleName : strRoles) {
                if (roleName.equals(RoleEnum.ROLE_ADMIN.name())) {
                    Role roleAsAdmin =
                            roleRepository.findByName(RoleEnum.ROLE_ADMIN).orElseThrow((throwsNoUserAdminError));
                    roles.add(roleAsAdmin);
                } else {
                    roles.add(optionalRoleAsUser.orElseThrow((throwsNoRoleUserError)));
                }
            }
        }
        user.setRoles(roles);
        userLoginRepository.save(user);
    }
}
