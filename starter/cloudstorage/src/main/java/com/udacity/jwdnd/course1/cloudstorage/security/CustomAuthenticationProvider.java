package com.udacity.jwdnd.course1.cloudstorage.security;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.service.HashService;
import com.udacity.jwdnd.course1.cloudstorage.service.UserService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final UserService userService;
    private final HashService hashService;

    public CustomAuthenticationProvider(UserService userService, HashService hashService) {
        this.userService = userService;
        this.hashService = hashService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String rawPassword = authentication.getCredentials().toString();

        User user = userService.getUser(username);

        if (user == null) {
            throw new BadCredentialsException("Username or password is invalid");
        }

        String hashedPassword = hashService.getHashedValue(rawPassword, user.getSalt());

        if (!hashedPassword.equals(user.getPassword())) {
            throw new BadCredentialsException("Invalid username or passowrd");
        }

        return new UsernamePasswordAuthenticationToken(username, rawPassword, Collections.emptyList());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}