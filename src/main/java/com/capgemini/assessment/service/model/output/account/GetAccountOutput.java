package com.capgemini.assessment.service.model.output.account;

import lombok.*;

/**
 * Created by silayugurlu on 5/29/18.
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class GetAccountOutput {

    private long id;
    private long customerId;
    private String currencyType;
    private long balance;
}
