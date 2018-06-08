package com.capgemini.assessment.web.rest;

import com.capgemini.assessment.service.AccountService;
import com.capgemini.assessment.service.exception.AccountNotFoundException;
import com.capgemini.assessment.service.exception.CustomerNotFoundException;
import com.capgemini.assessment.service.exception.InsufficientBalanceException;
import com.capgemini.assessment.service.model.input.account.AddAccountInput;
import com.capgemini.assessment.service.model.output.account.AddAccountOutput;
import com.capgemini.assessment.service.model.output.account.GetTransactionOutput;
import com.capgemini.assessment.web.rest.request.account.AddAccountRequest;
import com.capgemini.assessment.web.rest.response.account.AddAccountResponse;
import com.capgemini.assessment.web.rest.response.account.GetAccountTransactionResponse;
import com.capgemini.assessment.web.rest.response.account.TransactionResponse;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by silayugurlu on 5/27/18.
 */
@Slf4j
@RestController
public class AccountRest {


    @Autowired
    AccountService accountService;
    @Autowired
    private Tracer tracer;
    @Autowired
    @Qualifier("customerRestMapper")
    private MapperFacade mapperFacade;

    @ApiOperation(value = "add account for customer",
            notes = "add account for customer<br/>")
    @PostMapping("account")
    public AddAccountResponse addAccount(@ApiParam(value = "owner id, currency, amount") @RequestBody AddAccountRequest addAccountRequest) throws CustomerNotFoundException, AccountNotFoundException, InsufficientBalanceException {
        log.debug("addAccount method start", tracer.getCurrentSpan().getTraceId());
        AddAccountInput input = mapperFacade.map(addAccountRequest, AddAccountInput.class);
        AddAccountOutput output = accountService.addAccount(input);
        AddAccountResponse addAccountResponse = mapperFacade.map(output, AddAccountResponse.class);
        log.debug("addAccount method finish", tracer.getCurrentSpan().getTraceId());
        return addAccountResponse;
    }


    @ApiOperation(value = "get account transactions",
            notes = "get account transactions<br/>")
    @GetMapping("account/{id}")
    public GetAccountTransactionResponse getAccountTransactions(@ApiParam(value = "Account id") @PathVariable(name = "id") long accountId) throws AccountNotFoundException {
        log.debug("getAccountTransactions method start", tracer.getCurrentSpan().getTraceId());
        GetTransactionOutput output = accountService.getAccountTransactions(accountId);
        GetAccountTransactionResponse getAccountTransactionOutput = mapperFacade.map(output, GetAccountTransactionResponse.class);
        Optional.ofNullable(output).ifPresent(getAccountTransactionOutput1 -> Optional.ofNullable(getAccountTransactionOutput1.getAccountTransactionOutputs()).ifPresent(transactionOutputs -> {
            getAccountTransactionOutput.setTransactions(transactionOutputs.stream().map(transactionOutput -> mapperFacade.map(transactionOutput, TransactionResponse.class)).collect(Collectors.toList()));
        }));

        log.debug("getAccountTransactions method finish", tracer.getCurrentSpan().getTraceId());
        return getAccountTransactionOutput;

    }

}
