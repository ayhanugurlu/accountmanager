package com.capgemini.assessment.service;

import com.capgemini.assessment.data.entity.Account;
import com.capgemini.assessment.data.entity.Customer;
import com.capgemini.assessment.data.entity.Transaction;
import com.capgemini.assessment.data.repository.AccountRepository;
import com.capgemini.assessment.data.repository.CustomerRepository;
import com.capgemini.assessment.listener.ApplicationStartup;
import com.capgemini.assessment.service.exception.AccountNotFoundException;
import com.capgemini.assessment.service.exception.CustomerNotFoundException;
import com.capgemini.assessment.service.exception.ErrorCode;
import com.capgemini.assessment.service.exception.InsufficientBalanceException;
import com.capgemini.assessment.service.model.input.account.AddAccountInput;
import com.capgemini.assessment.service.model.input.transaction.TransactionInput;
import com.capgemini.assessment.service.model.output.account.AddAccountOutput;
import com.capgemini.assessment.service.model.output.account.GetAccountOutput;
import com.capgemini.assessment.service.model.output.account.GetTransactionOutput;
import com.capgemini.assessment.service.model.output.transaction.TransactionOutput;
import ma.glasnost.orika.MapperFacade;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;


/**
 * Created by Silay.Ugurlu on 28/05/2018
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountServiceTest {
    @Qualifier("accountServiceMapper")
    @Autowired
    MapperFacade mapperFacade;
    @MockBean
    AccountRepository accountRepository;
    @MockBean
    CustomerRepository customerRepository;
    @MockBean
    TransactionService transactionService;
    @Autowired
    private AccountService accountService;
    @MockBean
    private ApplicationStartup applicationStartup;

    @MockBean
    private Tracer tracer;

    @MockBean
    private Span span;

    @Before
    public void setUp() throws Exception {
        TransactionOutput transactionOutput = TransactionOutput.builder().id(1).build();
        when(span.getTraceId()).thenReturn(1l);
        when(tracer.getCurrentSpan()).thenReturn(span);
        Customer customer = Customer.builder().id(2).identityNumber("a").name("name").surname("surname").build();
        Account account = Account.builder().id(1).customer(customer).currency("TRY").balance(10).build();
        Transaction transaction = Transaction.builder().transactionDate(new Date()).account(account).amount(10).build();
        Set<Transaction> transactions = new HashSet<>();
        transactions.add(transaction);
        account.setTransactions(transactions);
        when(customerRepository.findOne(any(Long.class))).thenReturn(customer);
        when(accountRepository.save(any(Account.class))).thenReturn(account);
        when(accountRepository.findOne(1l)).thenReturn(null);
        when(accountRepository.findOne(2l)).thenReturn(account);
        List<Account> accounts = new ArrayList<>();
        accounts.add(account);
        when(accountRepository.findAccountsByCustomer_Id(any(Long.class))).thenReturn(accounts);
        when(transactionService.makeTransaction(any(TransactionInput.class))).thenReturn(transactionOutput);
    }

    @Test
    public void accountServiceTest() throws CustomerNotFoundException, AccountNotFoundException, InsufficientBalanceException {
        AddAccountInput addAccountInput = AddAccountInput.builder().amount(10).customerId(1).currency("TRY").build();
        AddAccountOutput addAccountOutput = accountService.addAccount(addAccountInput);
        Assert.assertEquals(addAccountOutput.getId(), 1l);
        Assert.assertEquals(addAccountOutput.getCurrency(), "TRY");
        Assert.assertEquals(addAccountOutput.getCustomerId(), 2);
        addAccountInput = AddAccountInput.builder().amount(-10).customerId(1).currency("TRY").build();
        try {
            addAccountOutput = accountService.addAccount(addAccountInput);
        } catch (InsufficientBalanceException insufficientBalanceException) {
            Assert.assertEquals(insufficientBalanceException.getErrorCode(), ErrorCode.INSUFFICENT_BALANCE);
        }

        List<GetAccountOutput> getAccountOutputs = accountService.getCustomerAccounts(1);
        Assert.assertEquals(getAccountOutputs.size(), 1);

        try {
            GetTransactionOutput getTransactionOutput = accountService.getAccountTransactions(1);
        } catch (AccountNotFoundException accountNotFoundException) {
            Assert.assertEquals(accountNotFoundException.getErrorCode(), ErrorCode.ACCOUNT_NOT_FOUND);
        }
        GetTransactionOutput getTransactionOutput = accountService.getAccountTransactions(2);
        Assert.assertEquals(getTransactionOutput.getAccountTransactionOutputs().size(), 1);

    }
}
