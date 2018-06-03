package com.capgemini.assessment.service.exception;

import org.springframework.http.HttpStatus;

import static com.capgemini.assessment.service.exception.ErrorCode.CUSTOMER_NOT_FOUND;

/**
 * Created by silayugurlu on 5/26/18.
 */
public class CustomerNotFoundException extends TemplateException {

    public CustomerNotFoundException(String identityNumber) {
        errors.add(String.format("Customer Not Found.  Identity Number %s", identityNumber));
    }

    public CustomerNotFoundException(long customerId) {
        errors.add(String.format("Customer Not Found.  Customer Id %d", customerId));
    }

    @Override
    public String getErrorCode() {
        return CUSTOMER_NOT_FOUND;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.NOT_FOUND;
    }
}
