package com.capgemini.assessment.service.mapper;

import com.capgemini.assessment.AccountManagerApplication;
import com.capgemini.assessment.data.entity.Account;
import com.capgemini.assessment.data.entity.Transaction;
import com.capgemini.assessment.service.model.input.transaction.TransactionInput;
import com.capgemini.assessment.service.model.output.account.AccountTransactionOutput;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

/**
 * Created by Silay.Ugurlu on 28/05/2018
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = AccountManagerApplication.class)
public class TransactionServiceMapperTest {


    @Autowired
    TransactionServiceMapper transactionServiceMapper;

    @Test
    public void shouldMapTransactionInputToTransaction() {
        TransactionInput transactionInput = TransactionInput.builder().accountId(1).amount(10).build();
        Transaction transaction = transactionServiceMapper.map(transactionInput, Transaction.class);
        Assert.assertEquals(transaction.getAmount(), transactionInput.getAmount());
        Assert.assertEquals(transaction.getAccount().getId(), transactionInput.getAccountId());
    }

    @Test
    public void shouldMapTransactionToTransactionResultOutput() {
        Transaction transaction = Transaction.builder().account(Account.builder().id(1).build()).transactionDate(new Date()).build();
        AccountTransactionOutput accountTransactionOutput = transactionServiceMapper.map(transaction, AccountTransactionOutput.class);
        Assert.assertEquals(transaction.getAmount(), accountTransactionOutput.getAmount());
        Assert.assertEquals(transaction.getTransactionDate(), accountTransactionOutput.getTransactionDate());
    }

}
