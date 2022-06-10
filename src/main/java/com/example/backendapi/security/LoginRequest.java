package com.example.backendapi.security;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class LoginRequest {
    @NotNull
    @NotBlank
    @Size(min = 1, max = 50)
    private String username;

    //Max size 71 because BCRYPT is limited to 72 bytes.
    @NotNull
    @NotBlank
    @Size( min = 1, max = 71)
    private String password;

    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
    public LoginRequest(){}

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }


}
