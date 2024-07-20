package org.k1.simplebankapp.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;
import org.k1.simplebankapp.entity.enums.TransactionStatus;
import org.k1.simplebankapp.entity.enums.TransactionType;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "transactions")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Where(clause = "deleted_date is null")
public class Transaction extends BaseDate {
    @Id
    private String id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "recipient_target_account", nullable = false)
    private Account recipientTargetAccount;

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @Column(nullable = false)
    private double amount;

    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionStatus status;

    @Column(name = "transaction_date")
    private Date transactionDate;

    private Double total;

    @Column(name = "is_balance_add", nullable = false)
    private boolean isBalanceAdd;

    @Column(nullable = false)
    private Date timestamp;
}


