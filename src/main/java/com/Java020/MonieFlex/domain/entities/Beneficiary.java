package com.Java020.MonieFlex.domain.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "beneficiary_tbl")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Beneficiary extends BaseClass{
    private String bankName;
    private String bankAccountNumber;
    private String fullName;
    private String phoneNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @OneToOne(mappedBy = "beneficiary")
    private Transaction transaction;


}
