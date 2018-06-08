package com.capgemini.assessment.web.rest.response.account;

import lombok.*;

import java.util.List;

/**
 * Created by Silay.Ugurlu on 28/05/2018
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class GetAccountTransactionResponse {
    private String name;
    private String surname;
    private long balance;
    private List<TransactionResponse> transactions;
}
