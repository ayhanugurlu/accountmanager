package com.capgemini.assessment.web.rest;

import com.capgemini.assessment.service.TransactionService;
import com.capgemini.assessment.service.exception.AccountNotFoundException;
import com.capgemini.assessment.service.exception.InsufficientBalanceException;
import com.capgemini.assessment.service.model.input.transaction.TransactionInput;
import com.capgemini.assessment.service.model.output.transaction.TransactionOutput;
import com.capgemini.assessment.web.rest.request.transaction.TransactionRequest;
import com.capgemini.assessment.web.rest.response.transaction.TransactionResponse;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by silayugurlu on 5/28/18.
 */
@Slf4j
@RestController
public class TransactionRest {


    @Autowired
    TransactionService transactionService;
    @Autowired
    private Tracer tracer;
    @Autowired
    @Qualifier("transactionRestMapper")
    private MapperFacade mapperFacade;

    @ApiOperation(value = "make transaction ",
            notes = "Makes transaction if account is available.<br/>")
    @PostMapping("transaction")
    public TransactionResponse makeTransaction(@ApiParam(value = "account id and amount") @RequestBody TransactionRequest transactionRequest) throws AccountNotFoundException, InsufficientBalanceException {
        log.debug("makeTransaction method start", tracer.getCurrentSpan().getTraceId());
        TransactionInput transactionInput = mapperFacade.map(transactionRequest, TransactionInput.class);
        TransactionOutput output = transactionService.makeTransaction(transactionInput);
        TransactionResponse transactionResponse = mapperFacade.map(output, TransactionResponse.class);
        log.debug("makeTransaction method finish", tracer.getCurrentSpan().getTraceId());
        return transactionResponse;

    }
}
