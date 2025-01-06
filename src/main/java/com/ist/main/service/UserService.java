package com.ist.main.service;

import com.ist.main.dto.UserRequestDto;
import com.ist.main.entity.User;
import com.ist.main.exception.BusinessException;
import com.ist.main.repository.IAccountRepository;
import com.ist.main.repository.IUserRepository;
import com.ist.main.service.interfaces.IUserService;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final IUserRepository userRepository;
    private final IAccountRepository accountRepository;

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findById(String id) throws BusinessException {
        return userRepository
                .findById(id)
                .orElseThrow(
                        () -> new BusinessException(HttpStatus.BAD_REQUEST, String.format("'%s' id not found", id)));
    }

    @Override
    public User add(UserRequestDto requestDto) {
        User user = new User();
        setUser(requestDto, user);
        return userRepository.save(user);
    }

    @Override
    public User update(String id, UserRequestDto requestDto) throws BusinessException {
        User userDb = userRepository
                .findById(id)
                .orElseThrow(
                        () -> new BusinessException(HttpStatus.BAD_REQUEST, String.format("'%s' id not found", id)));
        setUser(requestDto, userDb);
        return userRepository.save(userDb);
    }

    @Override
    @Transactional
    public void delete(String id) {
        userRepository
                .findById(id)
                .ifPresent(user -> accountRepository.findByUserId(user.getId()).forEach(accountRepository::delete));
        userRepository.deleteById(id);
    }

    private void setUser(UserRequestDto requestDto, User userDb) {
        userDb.setEmail(requestDto.getEmail());
        userDb.setName(requestDto.getName());
    }
}
