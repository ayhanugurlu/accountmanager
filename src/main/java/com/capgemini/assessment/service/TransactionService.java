package com.capgemini.assessment.service;

import com.capgemini.assessment.service.exception.AccountNotFoundException;
import com.capgemini.assessment.service.exception.InsufficientBalanceException;
import com.capgemini.assessment.service.model.input.transaction.TransactionInput;
import com.capgemini.assessment.service.model.output.transaction.TransactionOutput;

/**
 * Created by silayugurlu on 5/27/18.
 */
public interface TransactionService {

    /**
     *
     * @param transactionInout
     * @return
     * @throws AccountNotFoundException
     * @throws InsufficientBalanceException
     */
    TransactionOutput makeTransaction(TransactionInput transactionInout) throws AccountNotFoundException, InsufficientBalanceException;
}
