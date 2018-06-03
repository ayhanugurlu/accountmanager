package com.capgemini.assessment.service.exception;

import org.springframework.http.HttpStatus;

import static com.capgemini.assessment.service.exception.ErrorCode.ACCOUNT_NOT_FOUND;

/**
 * Created by silayugurlu on 5/26/18.
 *
 */
public class AccountNotFoundException extends TemplateException {

    public AccountNotFoundException(long accountId) {
        errors.add(String.format("Account Not Found.Account Id = %d", accountId));
    }

    @Override
    public String getErrorCode() {
        return ACCOUNT_NOT_FOUND;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.NOT_FOUND;
    }
}
