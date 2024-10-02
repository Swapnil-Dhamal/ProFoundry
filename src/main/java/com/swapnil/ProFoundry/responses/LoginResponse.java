package com.swapnil.ProFoundry.responses;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginResponse {

    private String username;
    private String email;
    private String sessionId;
    private String message;


    public LoginResponse(String message, Object o) {
    }
}
