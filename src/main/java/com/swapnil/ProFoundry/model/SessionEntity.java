package com.swapnil.ProFoundry.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "sessions")
public class SessionEntity {

    @Id
    private String sessionId;

    private Instant creationTime;
    private Instant lastAccessedTime;
    private Instant expirationTime;

    @DBRef
    private Users users;

}
