package com.rincentral.test.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ControllerExceptionHandler {

    private static final Logger LOGGER = LogManager.getLogger(ControllerExceptionHandler.class);

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public List<String> bindException(BindException e) {
        final List<String> messages = e.getBindingResult().getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());
        LOGGER.warn("{}\n{}", e.getClass().getName(), String.join("\n", messages));
        return messages;
    }

    //http://stackoverflow.com/a/22358422/548473
    // I use 404 in case if automated tests are used
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoSuchElementException.class)
    public String notFoundException(HttpServletRequest req, NoSuchElementException e) {
        final String msg = e.getLocalizedMessage();
        LOGGER.warn("{} {} \n {}", e.getClass().getName(), msg, req.getParameterMap());
        return msg;
    }
}