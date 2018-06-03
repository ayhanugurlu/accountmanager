package com.capgemini.assessment.web.rest.request.account;

import lombok.*;

/**
 * Created by silayugurlu on 5/26/18.
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class AddAccountRequest {
    private long customerId;
    private String currency;
    private long amount;
}
