package com.capgemini.assessment.web.rest.request.customer;

import lombok.*;

/**
 * Created by silayugurlu on 5/26/18.
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class AddCustomerRequest {
    private String identityNumber;
    private String name;
    private String surname;

}
