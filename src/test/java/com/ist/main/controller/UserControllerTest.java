package com.ist.main.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ist.main.dto.UserRequestDto;
import com.ist.main.entity.User;
import com.ist.main.exception.BusinessException;
import com.ist.main.service.interfaces.IUserService;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class UserControllerTest {

    private final IUserService userService = Mockito.mock(IUserService.class);

    private final UserController userController = new UserController(userService);

    private List<User> dummyUserList = new ArrayList<>();

    @Test
    void findAllSucceeded() {
        dummyUserList(5);
        Mockito.when(userService.findAll()).thenReturn(dummyUserList);

        ResponseEntity<List<User>> responseEntity = userController.findAll();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertTrue(responseEntity.getBody().stream()
                .allMatch(user -> user.getName().startsWith("dummy user")));
        Mockito.verify(userService, Mockito.times(1)).findAll();
    }

    @Test
    void addSucceeded() {
        dummyUserList(1);
        Mockito.when(userService.add(ArgumentMatchers.any(UserRequestDto.class)))
                .thenReturn(dummyUserList.get(0));
        ResponseEntity<User> response = userController.add(new UserRequestDto());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("dummy user1", response.getBody().getName());
        assertEquals("dummyuser1@gmail.com", response.getBody().getEmail());
        Mockito.verify(userService, Mockito.times(1)).add(ArgumentMatchers.any(UserRequestDto.class));
    }

    @Test
    void findByIdSucceeded() throws BusinessException {
        dummyUserList(1);
        Mockito.when(userService.findById(ArgumentMatchers.anyString())).thenReturn(dummyUserList.get(0));
        ResponseEntity<User> response = userController.findById("dummy-id");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("dummy user1", response.getBody().getName());
        assertEquals("dummyuser1@gmail.com", response.getBody().getEmail());
        Mockito.verify(userService, Mockito.times(1)).findById(ArgumentMatchers.anyString());
    }

    @Test
    void updateSucceeded() throws BusinessException {
        dummyUserList(1);
        Mockito.when(userService.update(ArgumentMatchers.anyString(), ArgumentMatchers.any(UserRequestDto.class)))
                .thenReturn(dummyUserList.get(0));
        ResponseEntity<User> response = userController.update("dummy-id", new UserRequestDto());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("dummy user1", response.getBody().getName());
        assertEquals("dummyuser1@gmail.com", response.getBody().getEmail());
        Mockito.verify(userService, Mockito.times(1))
                .update(ArgumentMatchers.anyString(), ArgumentMatchers.any(UserRequestDto.class));
    }

    @Test
    void deleteSucceeded() {
        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        Mockito.doNothing().when(userService).delete(ArgumentMatchers.anyString());
        userController.delete("dummyId");
        Mockito.verify(userService, Mockito.times(1)).delete(argumentCaptor.capture());
        assertEquals("dummyId", argumentCaptor.getValue());
    }

    private void dummyUserList(int record) {
        dummyUserList = new ArrayList<>();
        for (int i = 1; i <= record; i++) {
            User user = new User();
            user.setName("dummy user" + i);
            user.setEmail(String.format("dummyuser%s@gmail.com", i));
            dummyUserList.add(user);
        }
    }
}
