package com.swapnil.ProFoundry.repo;

import com.swapnil.ProFoundry.model.SessionEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionRepo extends MongoRepository<SessionEntity, String> {
}
