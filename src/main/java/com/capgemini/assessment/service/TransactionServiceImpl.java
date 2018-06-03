package com.capgemini.assessment.service;

import com.capgemini.assessment.data.entity.Account;
import com.capgemini.assessment.data.entity.Transaction;
import com.capgemini.assessment.data.repository.AccountRepository;
import com.capgemini.assessment.data.repository.TransactionRepository;
import com.capgemini.assessment.service.exception.AccountNotFoundException;
import com.capgemini.assessment.service.exception.InsufficientBalanceException;
import com.capgemini.assessment.service.model.input.transaction.TransactionInput;
import com.capgemini.assessment.service.model.output.transaction.TransactionOutput;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

/**
 * Created by silayugurlu on 5/27/18.
 */
@Slf4j
@Component
public class TransactionServiceImpl implements TransactionService {


    @Qualifier("accountServiceMapper")
    @Autowired
    MapperFacade mapperFacade;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    private Tracer tracer;

    @Transactional
    @Override
    public TransactionOutput makeTransaction(TransactionInput transactionInput) throws AccountNotFoundException, InsufficientBalanceException {
        Optional<Account> account = Optional.ofNullable(accountRepository.findOne(transactionInput.getAccountId()));
        account.orElseThrow(() -> new AccountNotFoundException(transactionInput.getAccountId()));
        long total = account.get().getBalance() + transactionInput.getAmount();
        if (total < 0) {
            throw new InsufficientBalanceException();
        }
        Transaction transaction = mapperFacade.map(transactionInput, Transaction.class);
        account.get().setBalance(total);
        transaction.setAccount(account.get());
        transaction.setTransactionDate(new Date());
        transaction = transactionRepository.save(transaction);
        TransactionOutput transactionOutput = mapperFacade.map(transaction, TransactionOutput.class);
        return transactionOutput;
    }
}
