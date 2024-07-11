package org.k1.simplebankapp.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;
import org.k1.simplebankapp.entity.base.BaseDate;
import org.k1.simplebankapp.enums.StatusTransaction;

import javax.persistence.*;
import java.math.BigInteger;

@Entity
@Table(name = "transaction")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Where(clause = "deleted_date is null")
public class Transaction extends BaseDate {

    @Id
    @Column(name = "transaction_id")
    private String transactionId;

    @Column(name = "transaction_type")
    private String transactionType;

    @Column(name = "amount")
    private BigInteger amount;

    @Column(name = "target_account")
    private String target;

    @Column(name = "desc")
    private String desc;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private StatusTransaction status;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;
}
