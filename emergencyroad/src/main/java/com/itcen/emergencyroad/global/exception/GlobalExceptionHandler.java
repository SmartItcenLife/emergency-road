package com.itcen.emergencyroad.global.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public String handleCustomException(CustomException e, Model model) {

        model.addAttribute("status",
                e.getExceptionStatus().getStatus().value());

        model.addAttribute("message",
                e.getExceptionStatus().getMessage());

        return "error/error-page";
    }
}