package com.example.backendapi.registration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
public class RegistrationRequest {
    @NotNull
    @NotBlank
    @Size( min = 2, max = 50)

    private String username;
    @NotNull
    @NotBlank
    @Size(max = 100)
    @Email
    private String email;

    //Max size 71 because BCRYPT is limited to 72 bytes.
    @NotNull
    @NotBlank
    @Size(max = 71)
    private String password;
}
