package com.swapnil.ProFoundry.repo;


import com.swapnil.ProFoundry.model.Users;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends MongoRepository<Users, String> {


    Users findByEmail(String email);

    Optional<Users> findByUsername(String identifier);

    Users findByUsernameOrEmail(String identifier, String identifier1);
}
