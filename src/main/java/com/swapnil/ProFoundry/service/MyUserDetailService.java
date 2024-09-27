package com.swapnil.ProFoundry.service;


import com.swapnil.ProFoundry.model.UserPrinciple;
import com.swapnil.ProFoundry.model.Users;
import com.swapnil.ProFoundry.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Primary
public class MyUserDetailService implements UserDetailsService {


    @Autowired
    UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            Users users=userRepo.findByEmail(username);

        if(users==null){
            throw new UsernameNotFoundException("User not found");
        }
        return new UserPrinciple(users);
    }
}
