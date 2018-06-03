package com.capgemini.assessment.data.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by silayugurlu on 5/27/18.
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
public class Transaction {

    @Id
    @GeneratedValue
    private long id;
    @ManyToOne(fetch = FetchType.LAZY)
    private Account account;
    private long amount;
    private Date transactionDate;
}
