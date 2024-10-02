package com.swapnil.ProFoundry.service;

import com.swapnil.ProFoundry.model.SessionEntity;
import com.swapnil.ProFoundry.model.Users;
import com.swapnil.ProFoundry.repo.SessionRepo;
import com.swapnil.ProFoundry.repo.UserRepo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class SessionService {


    private final UserRepo userRepo;
    private final SessionRepo sessionRepo;

    public void saveSessionData(HttpSession httpSession, String userId){


        SessionEntity sessionEntity=new SessionEntity();

        sessionEntity.setSessionId(httpSession.getId());
        sessionEntity.setCreationTime(Instant.ofEpochMilli(httpSession.getCreationTime()));
        sessionEntity.setLastAccessedTime(Instant.ofEpochMilli(httpSession.getLastAccessedTime()));

        Long expirationTime = httpSession.getCreationTime() + (httpSession.getMaxInactiveInterval() * 1000L);

        Users users=userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User Not Found"));
        sessionEntity.setUsers(users);

        sessionRepo.save(sessionEntity);

    }


}
