package com.capgemini.assessment.service.model.output.account;

/**
 * Created by silayugurlu on 5/26/18.
 */

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class AddAccountOutput {
    private long id;
    private long customerId;
    private String currency;
}
