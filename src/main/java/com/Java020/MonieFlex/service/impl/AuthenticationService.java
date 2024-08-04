package com.Java020.MonieFlex.service.impl;

import com.Java020.MonieFlex.Utils.AccountUtils;
import com.Java020.MonieFlex.domain.entities.Account;
import com.Java020.MonieFlex.domain.entities.ConfirmationToken;
import com.Java020.MonieFlex.domain.entities.Customer;
import com.Java020.MonieFlex.domain.enums.AccountType;
import com.Java020.MonieFlex.domain.enums.EmailTemplateName;
import com.Java020.MonieFlex.infrastructure.exception.CustomerNotFoundException;
import com.Java020.MonieFlex.infrastructure.exception.InvalidTokenException;
import com.Java020.MonieFlex.infrastructure.exception.PasswordMismatchException;
import com.Java020.MonieFlex.payload.request.AccountRequest;
import com.Java020.MonieFlex.infrastructure.config.JwtService;
import com.Java020.MonieFlex.payload.request.AuthenticationRequest;
import com.Java020.MonieFlex.payload.request.RegistrationRequest;
import com.Java020.MonieFlex.payload.response.AuthenticationResponse;
import com.Java020.MonieFlex.repository.AccountRepository;
import com.Java020.MonieFlex.repository.ConfirmationTokenRepository;
import com.Java020.MonieFlex.repository.CustomerRepository;
import com.Java020.MonieFlex.repository.RoleRepository;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomerRepository customerRepository;
    private final ConfirmationTokenRepository tokenRepository;
    private final EmailService emailService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final AccountUtils accountUtils;
    private final AccountRepository accountRepository;
    @Value("${frontend-host}")
    private String activateUrl;

    @Value("${frontend-reset-forgotten-password}")
    private String frontendResetPasswordUrl;

    public void register(RegistrationRequest request) throws MessagingException {
        var userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new IllegalStateException("Role USER was not initialized"));
        var user = Customer.builder()
                .firstName(request.getFirstName())
                .middleName(request.getMiddleName())
                .lastName(request.getLastName())
                .BVN(request.getBVN())
                .phoneNumber(request.getPhoneNumber())
                .address(request.getAddress())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .transactionPin(passwordEncoder.encode(request.getTransactionPin()))
                .accountLocked(false)
                .enabled(false)
                .roles(List.of(userRole))
                .build();
        customerRepository.save(user);
        sendValidationEmail(user);
    }

    private void sendValidationEmail(Customer user) throws MessagingException {
        var newToken = generateAndSaveActivationToken(user);
        Map<String, Object> properties = new HashMap<>();
        properties.put("confirmationUrl", activateUrl);
        properties.put("activation_code", newToken);

        emailService.sendEmail(
                user.getEmail(),
                user.fullName(),
                EmailTemplateName.ACTIVATE_ACCOUNT,
                "Account Activation",
                properties
        );
    }

    @Transactional
    private String generateAndSaveActivationToken(Customer user) {
        String generatedToken = generateActivationCode(6);
        var token = ConfirmationToken.builder()
                .token(generatedToken)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .customer(user)
                .build();
        try {
            tokenRepository.save(token);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return generatedToken;
    }

    private String generateActivationCode(int length) {
        String characters = "0123456789";
        StringBuilder codeBuilder = new StringBuilder();
        SecureRandom secureRandom = new SecureRandom();
        for (int i = 0; i < length; i++) {
            int randomIndex = secureRandom.nextInt(characters.length());
            codeBuilder.append(characters.charAt(randomIndex));
        }
        return codeBuilder.toString();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        Customer user = (Customer) auth.getPrincipal();
        Map<String, Object> claims = new HashMap<>();
        claims.put("fullName", user.fullName());
        String jwtToken = jwtService.generateToken(claims, user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    @Transactional
    public void activateAccount(String token) throws MessagingException {
        ConfirmationToken saveToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid Token"));
        if (LocalDateTime.now().isAfter(saveToken.getExpiresAt())) {
            sendValidationEmail(saveToken.getCustomer());
            throw new RuntimeException("Activation token has expired. A new token has been sent to the same email address");
        }
        Customer user = customerRepository.findById(saveToken.getCustomer().getId())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        user.setEnabled(true);
        customerRepository.save(user);
        createAccount(user);
        saveToken.setConfirmedAt(LocalDateTime.now());
        tokenRepository.save(saveToken);
    }

    private void createAccount(Customer user){
        Account newAccount = Account.builder()
                .accountNumber(accountUtils.generateAccountNumber(user.getEmail()))
                .accountBalance(BigDecimal.valueOf(100000))
                .accountType(AccountType.SAVINGS)
                .customer(user)
                .build();
        accountRepository.save(newAccount);
    }

    public String validateEmailForForgotPassword(String email) {
        return customerRepository.findByEmail(email)
                .map(customer->{
                    try {
                        sendResetPasswordValidationEmail(customer);
                    } catch (MessagingException e) {
                        throw new RuntimeException(e);
                    }
                    return "Reset password link has been sent";
                })
                .orElseThrow(()-> new CustomerNotFoundException(String.format("Customer with the email %s does not exist", email)));
    }

    public String resetForgottenPassword(String email, String token, String newPassword, String confirmPassword) throws MessagingException {
        ConfirmationToken saveToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new InvalidTokenException("Invalid Token"));
        if (LocalDateTime.now().isAfter(saveToken.getExpiresAt())) {
            throw new InvalidTokenException("Activation token has expired. A new token has been sent to the same email address");
        }
        var user = customerRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        if(!newPassword.equals(confirmPassword)){
            throw new PasswordMismatchException("Password does not match");
        }
        String hashedPassword= passwordEncoder.encode(newPassword);
        user.setPassword(hashedPassword);
        customerRepository.save(user);
        saveToken.setConfirmedAt(LocalDateTime.now());
        tokenRepository.save(saveToken);
        sendPasswordResetSuccessEmail(user);
        return "Password Reset Successfully";
    }
    private void sendPasswordResetSuccessEmail(Customer customer) throws MessagingException {
        emailService.sendEmail(
                customer.getEmail(),
                customer.fullName(),
                EmailTemplateName.PASSWORD_RESET_SUCCESS,
                "Reset Password Success",
                new HashMap<>()
        );
    }

    private void sendResetPasswordValidationEmail(Customer user) throws MessagingException {
        var newToken = generateAndSaveActivationToken(user);
        Map<String, Object> properties = new HashMap<>();
        properties.put("confirmationUrl", frontendResetPasswordUrl);
        properties.put("activation_code", newToken);

        emailService.sendEmail(
                user.getEmail(),
                user.fullName(),
                EmailTemplateName.RESET_FORGOTTEN_PASSWORD,
                "Reset Forgotten Password",
                properties
        );
    }

    public String resendOTP(String email) throws MessagingException {
        Customer user = customerRepository.findByEmail(email)
                .orElseThrow(() -> new CustomerNotFoundException(String.format("Customer with the email %s does not exist", email)));
        sendValidationEmail(user);
        return "OTP sent to your registered email";
    }
}
