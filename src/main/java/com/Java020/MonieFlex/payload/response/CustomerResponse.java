package com.Java020.MonieFlex.payload.response;

import lombok.Data;

@Data
public class CustomerResponse {
    private Long id;
    private String firstName;
    private String middleName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String address;

}