package com.capgemini.assessment.web.rest.response.account;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * Created by silayugurlu on 5/27/18.
 */
@Setter
@Getter
@NoArgsConstructor
public class TransactionResponse {

    private long amount;
    private Date transactionDate;

}
