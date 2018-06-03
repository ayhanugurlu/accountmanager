package com.capgemini.assessment.service.model.output.account;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Created by Silay.Ugurlu on 28/05/2018
 */
@Setter
@Getter
@NoArgsConstructor
public class GetTransactionOutput {
    private String name;
    private String surname;
    private long balance;
    private List<AccountTransactionOutput> accountTransactionOutputs;
}
