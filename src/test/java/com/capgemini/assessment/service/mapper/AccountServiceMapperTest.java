package com.capgemini.assessment.service.mapper;

import com.capgemini.assessment.AccountManagerApplication;
import com.capgemini.assessment.data.entity.Account;
import com.capgemini.assessment.data.entity.Customer;
import com.capgemini.assessment.data.entity.Transaction;
import com.capgemini.assessment.service.model.input.account.AddAccountInput;
import com.capgemini.assessment.service.model.output.account.AddAccountOutput;
import com.capgemini.assessment.service.model.output.account.GetTransactionOutput;
import com.capgemini.assessment.service.model.output.account.AccountTransactionOutput;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Silay.Ugurlu on 28/05/2018
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = AccountManagerApplication.class)
public class AccountServiceMapperTest {


    @Autowired
    AccountServiceMapper accountServiceMapper;

    @Test
    public void shouldMapAddCustomerAccountInputToAccount() {

        AddAccountInput addAccountInput = AddAccountInput.builder().customerId(1).currency("TRY").build();
        Account account = accountServiceMapper.map(addAccountInput, Account.class);
        Assert.assertEquals(account.getCustomer().getId(), addAccountInput.getCustomerId());
        Assert.assertEquals(account.getCurrency(), addAccountInput.getCurrency());
    }


    @Test
    public void shouldMapAddCustomerAccountToAddCustomerAccountOutput() {

        Customer customer = Customer.builder().id(1).identityNumber("a").name("name").surname("surname").build();
        Set<Transaction> transactions = new HashSet<>();
        Account account = Account.builder().id(1).customer(customer).transactions(transactions).currency("TRY").balance(10).build();
        transactions.add(Transaction.builder().id(11).account(account).amount(101).transactionDate(new Date()).build());
        transactions.add(Transaction.builder().id(12).account(account).amount(101).transactionDate(new Date()).build());

        AddAccountOutput addAccountOutput = accountServiceMapper.map(account, AddAccountOutput.class);
        Assert.assertEquals(addAccountOutput.getCustomerId(), account.getCustomer().getId());
        Assert.assertEquals(addAccountOutput.getCurrency(), account.getCurrency());
        Assert.assertEquals(addAccountOutput.getId(), account.getId());
    }

    @Test
    public void shouldMapAccountToGetAccountTransactionOutput() {
        Customer customer = Customer.builder().id(1).identityNumber("a").name("name").surname("surname").build();
        Set<Transaction> transactions = new HashSet<>();
        Account account = Account.builder().id(1).customer(customer).transactions(transactions).currency("TRY").balance(10).build();
        transactions.add(Transaction.builder().id(11).account(account).amount(101).transactionDate(new Date()).build());
        transactions.add(Transaction.builder().id(12).account(account).amount(101).transactionDate(new Date()).build());
        GetTransactionOutput getTransactionOutput = accountServiceMapper.map(account, GetTransactionOutput.class);
        Assert.assertEquals(getTransactionOutput.getName(), account.getCustomer().getName());
        Assert.assertEquals(getTransactionOutput.getSurname(), account.getCustomer().getSurname());
        Assert.assertEquals(getTransactionOutput.getBalance(), account.getBalance());

    }

    @Test
    public void shouldMapTransactionToTransactionOutput() {
        Customer customer = Customer.builder().id(1).identityNumber("a").name("name").surname("surname").build();
        Set<Transaction> transactions = new HashSet<>();
        Account account = Account.builder().id(1).customer(customer).transactions(transactions).currency("TRY").balance(10).build();
        transactions.add(Transaction.builder().id(11).account(account).amount(101).transactionDate(new Date()).build());
        Transaction transaction = Transaction.builder().id(12).account(account).amount(101).transactionDate(new Date()).build();
        transactions.add(transaction);
        AccountTransactionOutput accountTransactionOutput = accountServiceMapper.map(transaction, AccountTransactionOutput.class);
        Assert.assertEquals(accountTransactionOutput.getAmount(), transaction.getAmount());
        Assert.assertEquals(accountTransactionOutput.getTransactionDate(), transaction.getTransactionDate());
    }
}
