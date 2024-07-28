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
@Table(name = "bank_transfer")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Where(clause = "deleted_date is null")
public class BankTransfer extends BaseDate {

    @Id
    @Column(updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "bank_id", nullable = false)
    private Bank bank;

    @Column(name = "recipient_account_no", nullable = false)
    private String recipientAccountNo;

    @Column(name = "recipient_name", nullable = false)
    private String recipientName;
}
