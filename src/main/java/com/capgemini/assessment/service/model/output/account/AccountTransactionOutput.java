package com.capgemini.assessment.service.model.output.account;

import lombok.*;

import java.util.Date;

/**
 * Created by silayugurlu on 5/27/18.
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class AccountTransactionOutput {

    private long amount;
    private Date transactionDate;

}
