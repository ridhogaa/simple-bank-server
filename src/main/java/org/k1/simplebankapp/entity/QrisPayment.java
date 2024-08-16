package org.k1.simplebankapp.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;
import org.k1.simplebankapp.entity.enums.TransactionStatus;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "qris_payment")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Where(clause = "deleted_date is null")
public class QrisPayment extends BaseDate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "qris_code", unique = true, nullable = false)
    private String qrisCode;
    @Column(nullable = true)
    private Boolean isPaid;
    @Column(name = "expiration_time", nullable = true)
    private LocalDateTime expirationTime;
    private String accountNo;
    private Double amount;
}
