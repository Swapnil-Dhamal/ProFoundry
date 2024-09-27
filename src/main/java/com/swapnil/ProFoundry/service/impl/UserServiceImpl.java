package com.swapnil.ProFoundry.service.impl;


import com.swapnil.ProFoundry.model.Users;
import com.swapnil.ProFoundry.repo.UserRepo;
import com.swapnil.ProFoundry.requests.LoginRequest;
import com.swapnil.ProFoundry.requests.RegisterRequest;
import com.swapnil.ProFoundry.responses.LoginResponse;
import com.swapnil.ProFoundry.responses.RegisterResponse;
import com.swapnil.ProFoundry.responses.UserResponse;
import com.swapnil.ProFoundry.service.JWTService;
import com.swapnil.ProFoundry.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private JWTService jwtService;

    private final UserRepo userRepo;
    private final EmailService emailService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserServiceImpl(UserRepo userRepo, EmailService emailService) {
        this.userRepo = userRepo;
        this.emailService = emailService;
    }


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
    public List<UserResponse> getAllUsers(Users users) {
        List<Users> usersList = userRepo.findAll();


        return usersList.stream()
                .map(user -> new UserResponse(
                        user.getUsername())


                ).collect(Collectors.toList());
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


}
