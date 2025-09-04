package com.example.ecom.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ecom.dto.UserDTO;
import com.example.ecom.exception.InvalidCredentialsException;
import com.example.ecom.exception.UserAlreadyExistsException;
import com.example.ecom.model.User;
import com.example.ecom.repo.UserRepository;
import com.example.ecom.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public String register(UserDTO userDto) {
        if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("User already exists with email: " + userDto.getEmail());
        }

        User user = new User();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword()); // Consider encrypting
        user.setMobileNumber(userDto.getMobileNumber());
        userRepository.save(user);

        return "User registered successfully!";
    }

    @Override
    public String login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));

        if (!user.getPassword().equals(password)) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        return "mock-jwt-token-for-" + user.getEmail();
    }
}
