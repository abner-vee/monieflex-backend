package com.Java020.MonieFlex.infrastructure.controller;

import com.Java020.MonieFlex.service.impl.AuthenticationService;
import com.Java020.MonieFlex.payload.request.AuthenticationRequest;
import com.Java020.MonieFlex.payload.request.RegistrationRequest;
import com.Java020.MonieFlex.payload.response.AuthenticationResponse;
import com.Java020.MonieFlex.service.CustomerService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication")
public class AuthenticationController {
    private final AuthenticationService service;
    private final CustomerService customerService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> register(
            @RequestBody @Valid RegistrationRequest request
    ) throws MessagingException {
        service.register(request);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(
            @RequestBody @Valid AuthenticationRequest request
    ) {
        AuthenticationResponse response = service.authenticate(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/activate-account")
    public void confirm(
            @RequestParam String token
    ) throws MessagingException {
        service.activateAccount(token);
    }
    @GetMapping("/validate-email")
    public ResponseEntity<String> validateEmailForForgotPassword(@RequestParam String email){
        return new ResponseEntity<>(service.validateEmailForForgotPassword(email), HttpStatus.OK);
    }
    @PutMapping("/reset-forgotten-password")
    public ResponseEntity<String> resetForgottenPassword(@RequestParam String email,@RequestParam String token, @RequestParam String newPassword, @RequestParam String confirmPassword) throws MessagingException {
        return new ResponseEntity<>(service.resetForgottenPassword(email, token, newPassword, confirmPassword), HttpStatus.OK);
    }
}


