package com.capgemini.assessment.service.model.input.account;

import lombok.*;

/**
 * Created by silayugurlu on 5/26/18.
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class AddAccountInput {
    private long customerId;
    private String currency;
    private long amount;
}
