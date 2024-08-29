package com.ror.foodapp.service;

import com.ror.foodapp.model.SecurityUser;
import com.ror.foodapp.model.User;
import com.ror.foodapp.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Slf4j
@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);

        if(user == null) {
            //log.info("Email not found");
            throw new UsernameNotFoundException("User not found");
        }

        //log.info("User:{}", user);
        return new SecurityUser(user);
    }
}

