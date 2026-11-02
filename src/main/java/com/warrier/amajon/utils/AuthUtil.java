package com.warrier.amajon.utils;


import com.warrier.amajon.models.User;
import com.warrier.amajon.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class AuthUtil {

    @Autowired
    private UserRepository userRepository;

    public String loggedInEmail(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assert authentication != null;
        User user = userRepository.findByUserName(authentication.getName()).orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        return user.getEmail();
    }

    public Long loggedInUserId(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assert authentication != null;
        User user = userRepository.findByUserName(authentication.getName()).orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        return user.getUserId();
    }

    public User loggedInUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assert authentication != null;
        return userRepository.findByUserName(authentication.getName()).orElseThrow(() -> new UsernameNotFoundException("Username not found"));
    }


}
