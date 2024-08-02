package com.Java020.MonieFlex.service.impl;

import com.Java020.MonieFlex.domain.entities.Customer;
import com.Java020.MonieFlex.infrastructure.exception.CustomerNotFoundException;
import com.Java020.MonieFlex.infrastructure.exception.PasswordNotFoundException;
import com.Java020.MonieFlex.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;


class CustomerServiceImplTest {
    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private CustomerServiceImpl customerService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testResetPassword() {
        String email = "test@example.com";
        String oldPassword = "oldPassword";
        String newPassword = "newPassword";

        Customer mockCustomer = new Customer();
        mockCustomer.setEmail(email);
        mockCustomer.setPassword(passwordEncoder.encode(oldPassword));

        when(customerRepository.findByEmail(email)).thenReturn(Optional.of(mockCustomer));
        when(passwordEncoder.matches(oldPassword, mockCustomer.getPassword())).thenReturn(true);

        String result = customerService.resetPassword(email, oldPassword, newPassword);

        verify(customerRepository, times(1)).save(mockCustomer);

        assertEquals("Your Password has been reset successfully, login with the new password ", result);
    }

    @Test
    public void testResetPassword_CustomerNotFound() {
        String email = "nonexistent@example.com";
        String oldPassword = "oldPassword";
        String newPassword = "newPassword";
        when(customerRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () ->
                customerService.resetPassword(email, oldPassword, newPassword));
    }

    @Test
    public void testResetPassword_PasswordMismatch() {
        String email = "test@example.com";
        String oldPassword = "incorrectPassword";
        String newPassword = "newPassword";

        Customer mockCustomer = new Customer();
        mockCustomer.setEmail(email);
        mockCustomer.setPassword(passwordEncoder.encode("oldPassword"));

        when(customerRepository.findByEmail(email)).thenReturn(Optional.of(mockCustomer));
        when(passwordEncoder.matches(oldPassword, mockCustomer.getPassword())).thenReturn(false);

        assertThrows(PasswordNotFoundException.class, () ->
                customerService.resetPassword(email, oldPassword, newPassword));
    }

}