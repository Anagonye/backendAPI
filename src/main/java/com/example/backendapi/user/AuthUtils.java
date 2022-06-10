package com.example.backendapi.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthUtils {

    private static AppUserService service;

    @Autowired
    public AuthUtils(AppUserService appUserService) {
        AuthUtils.service = appUserService;
    }

    public static AppUser getCurrentPrincipal(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        return (AppUser) service.loadUserByUsername(currentPrincipalName);
    }
}
