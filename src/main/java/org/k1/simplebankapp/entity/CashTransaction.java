package org.k1.simplebankapp.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;
import org.k1.simplebankapp.entity.enums.MethodTransaction;
import org.k1.simplebankapp.entity.enums.TypeCashTransaction;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "cash_transaction")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Where(clause = "deleted_date is null")
public class CashTransaction extends BaseDate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "debit_account", nullable = false)
    private Account debitAccount;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "transaction_id", nullable = false)
    private Transaction transaction;

    @Enumerated(EnumType.STRING)
    private TypeCashTransaction typeCashTransaction;

    @Enumerated(EnumType.STRING)
    private MethodTransaction methodTransaction;

    @Column(name = "payment_code", nullable = false)
    private Long paymentCode;

    
}

