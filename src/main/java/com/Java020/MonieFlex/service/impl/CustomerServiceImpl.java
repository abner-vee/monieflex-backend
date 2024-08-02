package com.Java020.MonieFlex.service.impl;

import com.Java020.MonieFlex.domain.entities.Customer;
import com.Java020.MonieFlex.infrastructure.exception.CustomerNotFoundException;
import com.Java020.MonieFlex.infrastructure.exception.PasswordNotFoundException;
import com.Java020.MonieFlex.payload.response.CustomerResponse;
import com.Java020.MonieFlex.repository.CustomerRepository;
import com.Java020.MonieFlex.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    @Override
    public String resetPassword(String email, String oldPassword, String newPassword) {

            Customer user = customerRepository.findByEmail(email)
                    .orElseThrow(() -> new CustomerNotFoundException("No User with this email: " + email));
            if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
                throw new PasswordNotFoundException("Old password does not match the current password!");
            } else {
                user.setPassword(passwordEncoder.encode(newPassword));
                customerRepository.save(user);
                return "Your Password has been reset successfully, login with the new password ";
            }
    }
    @Override
    public CustomerResponse getUserById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id " + id));
        CustomerResponse customerResponse = new CustomerResponse();
        customerResponse.setId(customer.getId());
        customerResponse.setFirstName(customer.getFirstName());
        customerResponse.setMiddleName(customer.getMiddleName());
        customerResponse.setLastName(customer.getLastName());
        customerResponse.setEmail(customer.getEmail());
        customerResponse.setPhoneNumber(customer.getPhoneNumber());
        customerResponse.setAddress(customer.getAddress());
        return customerResponse;
    }


}
