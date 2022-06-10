package com.example.backendapi.user;

import com.example.backendapi.exceptions.RegistrationException;
import lombok.AllArgsConstructor;
import lombok.NonNull;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AppUserService implements UserDetailsService {

    @NonNull private final AppUserRepository repository;
    @NonNull private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("user with username " + username + " notfound"));

    }

    public void singUpUser(AppUser appUser){
       if(usernameTaken(appUser)){
           throw new RegistrationException("username already taken");
       }
       if(emailTaken(appUser)){
           throw new RegistrationException("email already taken");
       }
       appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
       repository.save(appUser);
    }

    public void deleteUserByEmail(String email){
        repository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(email));
        repository.deleteByEmail(email);
    }




    private boolean usernameTaken(AppUser appUser){
        return repository
                .findByUsername(appUser.getUsername())
                .isPresent();
    }

    private boolean emailTaken(AppUser appUser){
        return repository
                .findByEmail(appUser.getEmail())
                .isPresent();
    }
}
