package com.swapnil.ProFoundry.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "users")
public class Users {

    @Id
    private String id;

    @Field(name = "email")
    @Indexed(unique = true)
    private String email;

    @Field(name = "username")
    @Indexed(unique = true)
    private String username;

    @Field(name = "password")
    private String password;

    @Field(name = "emailOtp")
    private String emailOtp;

    @Field(name = "resetToken")
    private String resetToken;


    @Field(name = "emailVerified")
    private boolean emailVerified;

    @Field(name = "is2FAEnabled")
    private Boolean is2FAEnabled = false;


    private List<String> roles;

    @DBRef
    private List<SessionEntity> userSessions;


}



