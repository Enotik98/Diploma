package com.example.bookstore_app.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class CustomException extends RuntimeException{
    private HttpStatus status;
    private String message;

    public static String bindingResultToString(BindingResult bindingResult){
        List<String> errors = bindingResult.getAllErrors()
                .stream()
                .map(ObjectError::getDefaultMessage)
                .collect(Collectors.toList());
        return String.join("\n", errors);
    }

}
