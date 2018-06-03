package com.capgemini.assessment.service.exception;

import org.springframework.http.HttpStatus;

import static com.capgemini.assessment.service.exception.ErrorCode.INSUFFICENT_BALANCE;

/**
 * Created by silayugurlu on 5/27/18.
 */
public class InsufficientBalanceException extends TemplateException {

    public InsufficientBalanceException() {
        errors.add("Insufficient Balance");
    }

    @Override
    public String getErrorCode() {
        return INSUFFICENT_BALANCE;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
