package com.capgemini.assessment.service.model.input.customer;

/**
 * Created by silayugurlu on 5/26/18.
 */

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class AddCustomerInput {
    private String identityNumber;
    private String name;
    private String surname;

}
