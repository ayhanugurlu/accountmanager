package com.capgemini.assessment.service;

import com.capgemini.assessment.data.entity.Account;
import com.capgemini.assessment.data.repository.AccountRepository;
import com.capgemini.assessment.data.repository.CustomerRepository;
import com.capgemini.assessment.service.exception.AccountNotFoundException;
import com.capgemini.assessment.service.exception.CustomerNotFoundException;
import com.capgemini.assessment.service.exception.InsufficientBalanceException;
import com.capgemini.assessment.service.model.input.account.AddAccountInput;
import com.capgemini.assessment.service.model.input.transaction.TransactionInput;
import com.capgemini.assessment.service.model.output.account.AddAccountOutput;
import com.capgemini.assessment.service.model.output.account.GetAccountOutput;
import com.capgemini.assessment.service.model.output.account.GetTransactionOutput;
import com.capgemini.assessment.service.model.output.account.AccountTransactionOutput;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by silayugurlu on 5/27/18.
 */
@Slf4j
@Service
public class AccountServiceImpl implements AccountService {


    @Qualifier("accountServiceMapper")
    @Autowired
    MapperFacade mapperFacade;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    TransactionService transactionService;
    @Autowired
    private Tracer tracer;

    @Transactional
    @Override
    public AddAccountOutput addAccount(AddAccountInput addAccountInput) throws CustomerNotFoundException, AccountNotFoundException, InsufficientBalanceException {
        log.debug("addAccount method start", tracer.getCurrentSpan().getTraceId());

        //check if customer exists and amount is greater than 0 for the initial transaction
        Optional.ofNullable(customerRepository.findOne(addAccountInput.getCustomerId())).orElseThrow(() -> new CustomerNotFoundException(addAccountInput.getCustomerId()));

        if (addAccountInput.getAmount() < 0) { //can get an advance?
            throw new InsufficientBalanceException();
        }

        Account account = mapperFacade.map(addAccountInput, Account.class);
        account = accountRepository.save(account);

        //if initial amount is greater than 0, make first transaction
        if (addAccountInput.getAmount() > 0) {
            TransactionInput transactionInput = TransactionInput.builder().amount(addAccountInput.getAmount()).accountId(account.getId()).build();
            transactionService.makeTransaction(transactionInput);

        }
        AddAccountOutput addAccountOutput = mapperFacade.map(account, AddAccountOutput.class);
        log.debug("addAccount method finish", tracer.getCurrentSpan().getTraceId());
        return addAccountOutput;
    }


    @Transactional
    @Override
    public GetTransactionOutput getAccountTransactions(long accountId) throws AccountNotFoundException {
        log.debug("getAccountTransactions method start", tracer.getCurrentSpan().getTraceId());

        //check if account exists
        Account account = accountRepository.findOne(accountId);
        if (account == null) {
            throw new AccountNotFoundException(accountId);
        }
        GetTransactionOutput getTransactionOutput = mapperFacade.map(account, GetTransactionOutput.class);
        getTransactionOutput.setAccountTransactionOutputs(account.getTransactions().stream().map(transaction -> mapperFacade.map(transaction, AccountTransactionOutput.class)).collect(Collectors.toList()));
        log.debug("getAccountTransactions method finish", tracer.getCurrentSpan().getTraceId());
        return getTransactionOutput;
    }

    @Override
    public List<GetAccountOutput> getCustomerAccounts(long customerId) {
        log.debug("getCustomerAccounts method start", tracer.getCurrentSpan().getTraceId());
        List<Account> accounts = accountRepository.findAccountsByCustomer_Id(customerId);
        List<GetAccountOutput> result = accounts.stream().map(account -> mapperFacade.map(account, GetAccountOutput.class)).collect(Collectors.toList());
        log.debug("getCustomerAccounts method finish", tracer.getCurrentSpan().getTraceId());
        return result;
    }

}
