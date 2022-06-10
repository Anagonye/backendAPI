package com.example.backendapi.responseModel;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ConstraintViolationError {
    private final String field;
    private final String message;

}
