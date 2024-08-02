package com.Java020.MonieFlex.infrastructure.controller;

import com.Java020.MonieFlex.payload.response.CustomerResponse;
import com.Java020.MonieFlex.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/customer")
public class CustomerController {
    private final CustomerService customerService;
    @PutMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam String email, @RequestParam String oldPassword, @RequestParam String newPassword){
        return new ResponseEntity<>(customerService.resetPassword(email,oldPassword, newPassword), HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> getCustomerById(@PathVariable Long id) {
        CustomerResponse customerResponse = customerService.getUserById(id);
        return new ResponseEntity<>(customerResponse, HttpStatus.OK);
    }
}
