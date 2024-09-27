package com.swapnil.ProFoundry.controller;




import com.swapnil.ProFoundry.model.Users;
import com.swapnil.ProFoundry.requests.LoginRequest;
import com.swapnil.ProFoundry.requests.RegisterRequest;
import com.swapnil.ProFoundry.responses.LoginResponse;
import com.swapnil.ProFoundry.responses.RegisterResponse;
import com.swapnil.ProFoundry.responses.UserResponse;
import com.swapnil.ProFoundry.service.UserService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class AuthController {

    private final UserService userService;


    @PostMapping("/signUp")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest registerRequest){
        RegisterResponse response=userService.register(registerRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/verify")
    public ResponseEntity<String> verifyUser(@RequestParam String email, String emailOtp){

        try{
            userService.verify(email, emailOtp);
            return new ResponseEntity<>("User verified Successfully", HttpStatus.OK);
        }
        catch(RuntimeException e){
            LoginResponse errorResponse = new LoginResponse(e.getMessage(), null); // Assuming you have a constructor for this
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserResponse>> getUsers(Users users){
        return new ResponseEntity<>(userService.getAllUsers(users), HttpStatus.OK);
    }

}
