package org.example.flights;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.java.Log;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.RequestContextUtils;

import java.util.logging.Level;

@ControllerAdvice
@Log
public class ErrorControllerAdvice {

    @ExceptionHandler(NoResourceFoundException.class)
    public String handleNotFound(NoResourceFoundException e, HttpServletRequest request) {
        String uri = request.getRequestURI();

        if ("/favicon.ico".equals(uri)
                || "/apple-touch-icon.png".equals(uri)
                || "/apple-touch-icon-precomposed.png".equals(uri)) {
            return null;
        }

        log.log(Level.WARNING, "Resource not found: " + uri, e);
        RequestContextUtils.getOutputFlashMap(request)
                .put("errorMessage", "Requested resource was not found.");
        return "redirect:/tankincident";
    }

    @ExceptionHandler(ResponseStatusException.class)
    public String handleError(ResponseStatusException e, HttpServletRequest request) {
        log.log(Level.SEVERE, "Http error occurred: ", e);

        RequestContextUtils.getOutputFlashMap(request)
                .put("errorMessage", e.getReason() != null ? e.getReason() : "Request could not be completed.");
        return "redirect:/tankincident";
    }

    @ExceptionHandler(Exception.class)
    public String handleGeneralError(Exception e, HttpServletRequest request) {
        log.log(Level.SEVERE, "Unexpected error occurred: ", e);

        RequestContextUtils.getOutputFlashMap(request)
                .put("errorMessage", "An unexpected error occurred.");
        return "redirect:/tankincident";
    }
}