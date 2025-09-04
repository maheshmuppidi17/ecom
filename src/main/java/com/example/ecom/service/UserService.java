package com.example.ecom.service;

import com.example.ecom.dto.UserDTO;

public interface UserService {
    String register(UserDTO userDto);
    String login(String email, String password);
}
