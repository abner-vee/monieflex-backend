package com.Java020.MonieFlex.domain.entities;

import com.Java020.MonieFlex.domain.enums.CardType;
import com.Java020.MonieFlex.domain.enums.CardStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "card_tbl")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Card extends BaseClass {
    private String cardNumber;
    private String CVV;
    private CardType cardType;
    private LocalDate expirationDate;
    private CardStatus cardStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_number")
    private Account account;
}
