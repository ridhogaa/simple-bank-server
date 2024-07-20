package org.k1.simplebankapp.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;
import org.k1.simplebankapp.entity.enums.TransactionStatus;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "topup_ewallet")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Where(clause = "deleted_date is null")
public class TopupEwallet extends BaseDate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_account", nullable = false)
    private Account account;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ewallet_id", nullable = false)
    private Ewallet ewallet;

    @Column(name = "ewallet_number", nullable = false)
    private Long eWalletNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionStatus status;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "transaction_id", nullable = false)
    private Transaction transaction;

    @Column(nullable = false)
    private Date timestamp;

}
