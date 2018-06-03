package com.capgemini.assessment.web.rest.response.account;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by silayugurlu on 5/26/18.
 */
@Setter
@Getter
@NoArgsConstructor
public class AddAccountResponse {
    private long id;
    private long customerId;
    private String currency;

}
