package org.k1.simplebankapp.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;
import org.k1.simplebankapp.entity.base.BaseDate;

import javax.persistence.*;
import java.math.BigInteger;

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

    @Column(name = "type")
    private String type;
    @Column(name = "card_number")
    private String cardNumber;
    @Column(name = "exp_date")
    private String expDate;
    @Column(name = "balance")
    private BigInteger balance;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;
}
