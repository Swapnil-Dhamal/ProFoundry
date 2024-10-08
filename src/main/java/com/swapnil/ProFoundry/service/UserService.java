package com.swapnil.ProFoundry.service;


import com.swapnil.ProFoundry.model.Users;
import com.swapnil.ProFoundry.requests.LoginRequest;
import com.swapnil.ProFoundry.requests.RegisterRequest;
import com.swapnil.ProFoundry.responses.LoginResponse;
import com.swapnil.ProFoundry.responses.RegisterResponse;
import com.swapnil.ProFoundry.responses.UserResponse;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {

    RegisterResponse register(RegisterRequest request);

    void verify(String email, String emailOtp);

    List<UserResponse> getAllUsers();

    Users login(String identifier, String password);

    void configure(HttpSecurity http) throws Exception;

    void processForgotPassword(String email);

    void resetPassword(String token, String newPassword);
}
