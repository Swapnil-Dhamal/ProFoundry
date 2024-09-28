package com.swapnil.ProFoundry.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "sessions")
public class UserSession {

    @Id
    private Long id;

    @Field(name="sessionId")
    private String sessionId;

    @Field(name="createdAt")
    private Date createdAt;

    @Field(name="invalidatedAt")
    private Date invalidated;

    @DBRef
    private Users users;

}
