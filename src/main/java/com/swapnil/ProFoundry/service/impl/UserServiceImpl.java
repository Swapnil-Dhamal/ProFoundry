package com.swapnil.ProFoundry.service.impl;


import com.swapnil.ProFoundry.model.Users;
import com.swapnil.ProFoundry.repo.UserRepo;
import com.swapnil.ProFoundry.requests.RegisterRequest;
import com.swapnil.ProFoundry.responses.RegisterResponse;
import com.swapnil.ProFoundry.responses.UserResponse;
import com.swapnil.ProFoundry.service.EmailService;
//import com.swapnil.ProFoundry.service.JWTService;
import com.swapnil.ProFoundry.service.JWTService;
import com.swapnil.ProFoundry.service.UserService;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

import java.util.Random;

import java.util.UUID;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {


    private final JWTService jwtService;
    private final UserRepo userRepo;
    private final EmailService emailService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;




    @Override
    public RegisterResponse register(RegisterRequest request) {
        Users existingUser=userRepo.findByEmail(request.getEmail());

        if(existingUser !=null && existingUser.isEmailVerified()){
            throw new RuntimeException("User Already Register");
        }

        Users users=Users
                .builder()
                .username(request.getUsername())
                .password(bCryptPasswordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .build();

        String otp=generateOtp();
        users.setEmailOtp(otp);
        Users savedUser= userRepo.save(users);


        sendEmailVerification(savedUser.getEmail(), otp);


        String token= jwtService.generateToken(users.getUsername());

        RegisterResponse res= RegisterResponse
                .builder()
                .username(users.getUsername())
                .email(users.getEmail())
                .token(token)
                .build();

        return res;
    }

    @Override
    public void verify(String email, String emailOtp) {

        Users users=userRepo.findByEmail(email);

        if(users==null){
            throw new RuntimeException("User Not Found");
        }
        else if(users.isEmailVerified()){
            throw new RuntimeException("User is already verified");
        }
        else if(emailOtp.equals(users.getEmailOtp())){
            users.setEmailVerified(true);
            userRepo.save(users);

        }
        else{
            throw new RuntimeException("Internal server error");
        }
    }

    @Override
    public List<UserResponse> getAllUsers() {
        List<Users> usersList = userRepo.findAll();


        return usersList.stream()
                .map(user -> new UserResponse(
                        user.getUsername())


                ).collect(Collectors.toList());
    }

    @Override
    public Users login(String identifier, String password) {

        Users user = userRepo.findByUsernameOrEmail(identifier, identifier);

        if(user!=null && bCryptPasswordEncoder.matches(password, user.getPassword())){
            return user;
        }
        else {
            return null;
            }

    }

    private String generateOtp(){
        Random random=new Random();
        int otpValue=100000 + random.nextInt(999999);
        return String.valueOf(otpValue);
    }

//    private String generateSms(){
//        Random random=new Random();
//        int otpValue=100000 + random.nextInt(999999);
//        return String.valueOf(otpValue);
//    }

    public void sendEmailVerification(String email, String otp){
        String subject="Email Verification";
        String body="Your verification otp is "+otp;
        emailService.sendEmail(email, subject, body);
    }




    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .addFilterBefore(new GenericFilter() {
                    @Override
                    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException, ServletException, IOException {
                        HttpServletRequest httpRequest = (HttpServletRequest) request;
                        System.out.println("Request URI: " + httpRequest.getRequestURI());
                        chain.doFilter(request, response);
                    }
                }, UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    public void processForgotPassword(String email) {
        Users users=userRepo.findByEmail(email);
        if(users != null){
            String token= UUID.randomUUID().toString();
            users.setResetToken(token);
            userRepo.save(users);

            String resetLink = "http://localhost:8080/auth/reset-password?token=" + token;
            emailService.sendEmail(email, "Password Reset Request", "To reset your password, click the following link: "+ resetLink);

        }
    }

    public void resetPassword(String token, String newPassword){
        Users users=userRepo.findByResetToken(token);

        if(users!= null){
            users.setPassword(newPassword);
            users.setResetToken(null);
            userRepo.save(users);
        }
        else{
            throw new IllegalArgumentException("Invalid reset token");
        }
    }



}
