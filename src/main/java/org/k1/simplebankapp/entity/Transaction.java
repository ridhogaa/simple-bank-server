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

    @Column(name = "recipient_target_account", nullable = false)
    private String recipientTargetAccount;

    @Column(name = "recipient_target_name", nullable = false)
    private String recipientTargetName;

    @Column(name = "recipient_target_type", nullable = false)
    private String recipientTargetType;

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @Column(nullable = false)
    private double amount;

    @Column(nullable = true)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionStatus status;

    @Column(name = "no_ref", nullable = false)
    private String noRef;

    @Column(name = "fee_admin", nullable = true)
    private Double feeAdmin;
}


