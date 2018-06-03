package com.capgemini.assessment.service;

import com.capgemini.assessment.service.exception.AccountNotFoundException;
import com.capgemini.assessment.service.exception.CustomerNotFoundException;
import com.capgemini.assessment.service.exception.InsufficientBalanceException;
import com.capgemini.assessment.service.model.input.account.AddAccountInput;
import com.capgemini.assessment.service.model.output.account.AddAccountOutput;
import com.capgemini.assessment.service.model.output.account.GetAccountOutput;
import com.capgemini.assessment.service.model.output.account.GetTransactionOutput;

import java.util.List;

/**
 * Created by silayugurlu on 5/27/18.
 *
 */
public interface AccountService {
    /**
     * Adds new account to customer and make initial transaction if initial balance > 0
     *
     * @param addAccountInput
     * @return
     * @throws CustomerNotFoundException
     * @throws AccountNotFoundException
     * @throws InsufficientBalanceException
     */
    AddAccountOutput addAccount(AddAccountInput addAccountInput) throws CustomerNotFoundException, AccountNotFoundException, InsufficientBalanceException;


    /**
     * Lists transactions of an account
     * @param accountId
     * @return
     * @throws AccountNotFoundException
     */
    GetTransactionOutput getAccountTransactions(long accountId) throws AccountNotFoundException;

    /**
     * Lists accounts of a customer
     * @param customerId
     * @return
     */
    List<GetAccountOutput> getCustomerAccounts(long customerId);
}
