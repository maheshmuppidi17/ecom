package com.example.ecom.serviceImpl;

import com.example.ecom.dto.UserDTO;
import com.example.ecom.exception.InvalidCredentialsException;
import com.example.ecom.exception.UserAlreadyExistsException;
import com.example.ecom.model.User;
import com.example.ecom.repo.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private UserDTO userDto;
    private User savedUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        userDto = new UserDTO();
        userDto.setName("John Doe");
        userDto.setEmail("john@example.com");
        userDto.setMobileNumber("9999999999");
        userDto.setPassword("plainPass");

        savedUser = new User();
        savedUser.setId("testUserId");
        savedUser.setName("John Doe");
        savedUser.setEmail("john@example.com");
        savedUser.setMobileNumber("9999999999");
        savedUser.setPassword("plainPass"); // plain password
    }

    // ----- register() -----
    @Test
    void testRegister_Success() {
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenAnswer(inv -> {
            User u = inv.getArgument(0);
            u.setId("testUserId");
            return u;
        });

        String result = userService.register(userDto);

        assertEquals("User registered successfully!", result);
        verify(userRepository).findByEmail("john@example.com");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testRegister_EmailAlreadyExists() {
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(savedUser));

        UserAlreadyExistsException ex = assertThrows(UserAlreadyExistsException.class,
                () -> userService.register(userDto));

        assertEquals("User already exists with email: john@example.com", ex.getMessage());
        verify(userRepository).findByEmail("john@example.com");
        verify(userRepository, never()).save(any());
    }

    // ----- login() -----
    @Test
    void testLogin_Success() {
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(savedUser));

        String token = userService.login("john@example.com", "plainPass");

        assertEquals("mock-jwt-token-for-john@example.com", token);
        verify(userRepository).findByEmail("john@example.com");
    }

    @Test
    void testLogin_UserNotFound() {
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.empty());

        InvalidCredentialsException ex = assertThrows(InvalidCredentialsException.class,
                () -> userService.login("john@example.com", "plainPass"));

        assertEquals("Invalid email or password", ex.getMessage());
        verify(userRepository).findByEmail("john@example.com");
    }

    @Test
    void testLogin_InvalidCredentials() {
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(savedUser));

        InvalidCredentialsException ex = assertThrows(InvalidCredentialsException.class,
                () -> userService.login("john@example.com", "wrongPass"));

        assertEquals("Invalid email or password", ex.getMessage());
        verify(userRepository).findByEmail("john@example.com");
    }
}
