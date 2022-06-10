package com.example.backendapi.registration;

import com.example.backendapi.user.AppUser;
import com.example.backendapi.user.AppUserService;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RegistrationServiceImpl implements RegistrationService{

    @NonNull private final AppUserService appUserService;


    @Override
    public void register(RegistrationRequest request) {
        appUserService.singUpUser(new AppUser(
                request.getUsername(),
                request.getPassword(),
                request.getEmail(),
                "USER"
        ));
    }
}
