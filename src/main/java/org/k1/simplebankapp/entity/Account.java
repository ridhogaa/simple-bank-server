package org.k1.simplebankapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;
import org.k1.simplebankapp.entity.enums.AccountType;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "account")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Where(clause = "deleted_date is null")
public class Account extends BaseDate {

    @Id
    @Column(name = "no")
    private String no;

    @Column(nullable = false)
    private double balance;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountType accountType;

    @Column(name = "atm_card_no", length = 100, nullable = false)
    private String cardNumber;

    @Column(name = "exp_date", nullable = false)
    private Date expDate;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "bank_id")
    private Bank bank;

    @Column(name = "pin", length = 6)
    private String pin;

    @JsonIgnore
    @Column(name = "pin_attempts", nullable = false)
    private Integer pinAttempts = 0;

    private static final int MAX_PIN_ATTEMPTS = 3;

    public boolean validatePin(String pin) {
        if (this.pin.equals(pin)) {
            // Reset attempts on successful validation
            this.pinAttempts = 0;
            return true;
        } else {
            // Increment attempts on failure
            this.pinAttempts++;
            if (this.pinAttempts >= MAX_PIN_ATTEMPTS) {
                // Account locked due to too many attempts
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Your account is locked due to too many failed PIN attempts. Please try again later.");
            }
            return false;
        }
    }
}
