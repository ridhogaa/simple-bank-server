package org.k1.simplebankapp.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;
import org.k1.simplebankapp.entity.enums.MutationType;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "mutation")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Where(clause = "deleted_date is null")
public class Mutation extends BaseDate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "transaction_id", nullable = false)
    private Transaction transaction;

    @Enumerated(EnumType.STRING)
    @Column(name = "mutation_type", nullable = false)
    private MutationType mutationType;

    @Column(nullable = false)
    private Date timestamp;

}


