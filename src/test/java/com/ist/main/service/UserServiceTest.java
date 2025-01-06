package com.ist.main.service;

import static org.junit.jupiter.api.Assertions.*;

import com.ist.main.dto.UserRequestDto;
import com.ist.main.entity.Account;
import com.ist.main.entity.User;
import com.ist.main.exception.BusinessException;
import com.ist.main.repository.IAccountRepository;
import com.ist.main.repository.IUserRepository;
import com.ist.main.service.interfaces.IUserService;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;

class UserServiceTest {

    private final IUserRepository userRepository = Mockito.mock(IUserRepository.class);
    private final IAccountRepository accountRepository = Mockito.mock(IAccountRepository.class);
    private final IUserService userService = new UserService(userRepository, accountRepository);

    @Test
    void findAll() {
        List<User> dummyUserList = dummyUserList(10);
        Mockito.when(userRepository.findAll()).thenReturn(dummyUserList);
        List<User> users = userService.findAll();
        Assertions.assertEquals(10, users.size());
        assertTrue(users.stream().allMatch(user -> user.getName().startsWith("dummy user")));
        assertTrue(users.stream().allMatch(user -> user.getEmail().startsWith("dummyuser")));
        assertTrue(users.stream().allMatch(user -> user.getEmail().endsWith("@gmail.com")));
    }

    @Test
    void findById() throws BusinessException {
        User userDummy = dummyUserList(1).get(0);
        Mockito.when(userRepository.findById(ArgumentMatchers.anyString())).thenReturn(Optional.of(userDummy));
        User user = userService.findById(userDummy.getId());
        Assertions.assertEquals("dummy user1", user.getName());
        Assertions.assertTrue(user.getEmail().startsWith("dummyuser"));
        Assertions.assertTrue(user.getEmail().endsWith("@gmail.com"));
        Mockito.verify(userRepository, Mockito.times(1)).findById(ArgumentMatchers.eq(userDummy.getId()));
    }

    @Test
    void add() {
        List<User> dummyUserList = dummyUserList(1);
        Mockito.when(userRepository.save(ArgumentMatchers.any(User.class))).thenReturn(dummyUserList.get(0));
        User user = userService.add(new UserRequestDto());
        Assertions.assertEquals(user.getId(), dummyUserList.get(0).getId());
        Assertions.assertEquals(user.getEmail(), dummyUserList.get(0).getEmail());
        Assertions.assertEquals(user.getName(), dummyUserList.get(0).getName());
        Mockito.verify(userRepository, Mockito.times(1)).save(ArgumentMatchers.any(User.class));
    }

    @Test
    void update() throws BusinessException {
        User userDummy = dummyUserList(1).get(0);
        UserRequestDto userRequestDto = new UserRequestDto();
        userRequestDto.setName("cokro");
        userRequestDto.setEmail("cokro@gmail.com");
        Mockito.when(userRepository.findById(ArgumentMatchers.eq(userDummy.getId())))
                .thenReturn(Optional.of(userDummy));
        Mockito.when(userRepository.save(ArgumentMatchers.any(User.class))).thenReturn(userDummy);
        User user = userService.update(userDummy.getId(), userRequestDto);
        Assertions.assertEquals(userDummy.getId(), user.getId());
        Assertions.assertEquals("cokro", user.getName());
        Assertions.assertEquals("cokro@gmail.com", user.getEmail());
    }

    @Test
    void updateFailed() {
        UserRequestDto userRequestDto = new UserRequestDto();
        userRequestDto.setName("cokro");
        userRequestDto.setEmail("cokro@gmail.com");
        Mockito.when(userRepository.findById("dummy-id")).thenReturn(Optional.empty());
        BusinessException businessException =
                Assertions.assertThrows(BusinessException.class, () -> userService.update("dummy-id", userRequestDto));
        assertEquals("'dummy-id' id not found", businessException.getErrorMessage());
        assertEquals(HttpStatus.BAD_REQUEST, businessException.getHttpStatus());
    }

    @Test
    void delete() {
        List<User> dummyUserList = dummyUserList(1);
        Account account = new Account();
        account.setId("dummy-account-id");
        account.setBalance(BigDecimal.ONE);
        User dummyUser = dummyUserList.get(0);
        account.setUser(dummyUser);
        Mockito.when(userRepository.findById(ArgumentMatchers.anyString())).thenReturn(Optional.of(dummyUser));
        Mockito.when(accountRepository.findByUserId(dummyUser.getId())).thenReturn(List.of(account));
        Mockito.doNothing().when(accountRepository).delete(ArgumentMatchers.any(Account.class));
        Mockito.doNothing().when(userRepository).delete(ArgumentMatchers.any(User.class));

        Assertions.assertDoesNotThrow(() -> userService.delete(dummyUser.getId()));
        Mockito.verify(userRepository, Mockito.times(1)).findById(ArgumentMatchers.eq(dummyUser.getId()));
        Mockito.verify(accountRepository, Mockito.times(1)).findByUserId(ArgumentMatchers.eq(dummyUser.getId()));
        Mockito.verify(accountRepository, Mockito.times(1)).delete(ArgumentMatchers.any(Account.class));
        Mockito.verify(userRepository, Mockito.times(1)).deleteById(ArgumentMatchers.eq(dummyUser.getId()));
    }

    private List<User> dummyUserList(int record) {
        List<User> dummyUserList = new ArrayList<>();
        for (int i = 1; i <= record; i++) {
            User user = new User();
            user.setId(UUID.randomUUID().toString());
            user.setName("dummy user" + i);
            user.setEmail(String.format("dummyuser%s@gmail.com", i));
            dummyUserList.add(user);
        }
        return dummyUserList;
    }
}
