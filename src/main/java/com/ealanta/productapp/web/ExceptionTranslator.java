package com.ealanta.productapp.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ControllerAdvice
public class ExceptionTranslator {

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseBody
    public ResponseEntity<String> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        Class<?> type = e.getRequiredType();
        e.getRequiredType();
        final String message;
        if(type.isEnum()){
            Stream<String> stream = Arrays.stream(type.getEnumConstants())
                                            .map(Object::toString);
            String withComma=stream.collect(Collectors.joining(", "));
            message = "The parameter " + e.getName() + " must have a value among : " + withComma;
        } else {
            message = "The parameter " + e.getName() + " must be of type " + type.getTypeName();
        }
        return ResponseEntity.unprocessableEntity().body(message);
    }
}