package com.ist.main.service.interfaces;

import com.ist.main.dto.UserRequestDto;
import com.ist.main.entity.User;
import com.ist.main.exception.BusinessException;
import java.util.List;

public interface IUserService {

    List<User> findAll();

    User findById(String id) throws BusinessException;

    User add(UserRequestDto requestDto);

    User update(String id, UserRequestDto requestDto) throws BusinessException;

    void delete(String id);
}
