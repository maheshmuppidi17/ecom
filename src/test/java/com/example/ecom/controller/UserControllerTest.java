package com.example.ecom.controller;

import com.example.ecom.contrller.UserController;
import com.example.ecom.dto.UserDTO;
import com.example.ecom.exception.InvalidCredentialsException;
import com.example.ecom.exception.UserAlreadyExistsException;
import com.example.ecom.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegister_Success() {
        UserDTO userDto = new UserDTO();
        userDto.setEmail("test@example.com");
        userDto.setPassword("password");
        userDto.setName("John Doe");

        String expectedMessage = "User registered successfully!";

        when(userService.register(userDto)).thenReturn(expectedMessage);

        ResponseEntity<String> response = userController.register(userDto);

        assertEquals(expectedMessage, response.getBody());
        verify(userService, times(1)).register(userDto);
    }

    @Test
    void testRegister_UserAlreadyExists() {
        UserDTO userDto = new UserDTO();
        userDto.setEmail("existing@example.com");
        userDto.setPassword("password");
        userDto.setName("Jane Doe");

        when(userService.register(userDto))
                .thenThrow(new UserAlreadyExistsException("User already exists with email: existing@example.com"));

        try {
            userController.register(userDto);
        } catch (UserAlreadyExistsException ex) {
            assertEquals("User already exists with email: existing@example.com", ex.getMessage());
        }

        verify(userService, times(1)).register(userDto);
    }

    @Test
    void testLogin_Success() {
        String email = "test@example.com";
        String password = "password";
        String expectedToken = "mock-jwt-token-for-test@example.com";

        when(userService.login(email, password)).thenReturn(expectedToken);

        ResponseEntity<String> response = userController.login(email, password);

        assertEquals(expectedToken, response.getBody());
        verify(userService, times(1)).login(email, password);
    }

    @Test
    void testLogin_InvalidCredentials() {
        String email = "unknown@example.com";
        String password = "wrongpass";

        when(userService.login(email, password))
                .thenThrow(new InvalidCredentialsException("Invalid email or password"));

        try {
            userController.login(email, password);
        } catch (InvalidCredentialsException ex) {
            assertEquals("Invalid email or password", ex.getMessage());
        }

        verify(userService, times(1)).login(email, password);
    }
}
