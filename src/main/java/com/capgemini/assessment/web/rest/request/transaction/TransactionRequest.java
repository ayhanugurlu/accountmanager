package com.capgemini.assessment.web.rest.request.transaction;

import lombok.*;

/**
 * Created by silayugurlu on 5/28/18.
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class TransactionRequest {
    private long accountId;
    private long amount;
}
