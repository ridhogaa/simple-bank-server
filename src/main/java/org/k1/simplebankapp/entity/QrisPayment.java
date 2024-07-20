package org.k1.simplebankapp.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;
import org.k1.simplebankapp.entity.enums.TransactionStatus;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "qris_payment")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Where(clause = "deleted_date is null")
public class QrisPayment extends BaseDate{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long qrisPaymentId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "transaction_id", nullable = false)
    private Transaction transaction;

    @Column(name = "recipient_account_no", nullable = false)
    private Long recipientAccountNo;

    @Column(nullable = false)
    private Long amount;

    @Column(name = "payment_date", nullable = false)
    private Date paymentDate;

    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

}
