package com.argha.Event_Management_System.security;

import com.argha.Event_Management_System.entity.User;
import com.argha.Event_Management_System.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private AuthService authService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = authService.findUserByEmail(email);
        if(user==null)
            throw new UsernameNotFoundException("User not found");
        return new UserPrincipal(user);
    }
}
