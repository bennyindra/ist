package com.ist.main.service.interfaces;

import com.ist.main.dto.SignupRequestDto;
import com.ist.main.exception.ApplicationException;
import com.ist.main.exception.BusinessException;

public interface IUserLoginService {

    void registerLogin(SignupRequestDto requestDto) throws BusinessException, ApplicationException;
}
