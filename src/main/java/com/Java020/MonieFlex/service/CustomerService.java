package com.Java020.MonieFlex.service;

import com.Java020.MonieFlex.payload.response.CustomerResponse;

public interface CustomerService {

    String resetPassword(String email, String oldPassword, String newPassword);
    CustomerResponse getUserById(Long id);
}
