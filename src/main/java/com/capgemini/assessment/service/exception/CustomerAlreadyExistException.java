package com.capgemini.assessment.service.exception;

import org.springframework.http.HttpStatus;

import static com.capgemini.assessment.service.exception.ErrorCode.CUSTOMER_ALREADY_EXIST;

/**
 * Created by silayugurlu on 5/26/18.
 */
public class CustomerAlreadyExistException extends TemplateException {

    public CustomerAlreadyExistException(String identityNumber) {
        errors.add(String.format("Customer Already Exist. Identity Number %s", identityNumber));
    }

    @Override
    public String getErrorCode() {
        return CUSTOMER_ALREADY_EXIST;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.CONFLICT;
    }
}
