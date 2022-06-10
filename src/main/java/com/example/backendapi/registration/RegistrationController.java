package com.example.backendapi.registration;

import com.example.backendapi.responseModel.Response;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/registration")
@AllArgsConstructor
public class RegistrationController {


    @NonNull private final RegistrationService registrationService;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Response register(@Valid @RequestBody RegistrationRequest request){
        registrationService.register(request);
        return new Response("registration successful", HttpStatus.CREATED);
    }

}
