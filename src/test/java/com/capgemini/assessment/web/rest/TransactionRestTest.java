package com.capgemini.assessment.web.rest;

import com.capgemini.assessment.AccountManagerApplication;
import com.capgemini.assessment.web.rest.request.account.AddAccountRequest;
import com.capgemini.assessment.web.rest.request.customer.AddCustomerRequest;
import com.capgemini.assessment.web.rest.request.transaction.TransactionRequest;
import com.capgemini.assessment.web.rest.response.account.AddAccountResponse;
import com.capgemini.assessment.web.rest.response.customer.AddCustomerResponse;
import com.capgemini.assessment.web.rest.response.transaction.TransactionResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by silayugurlu on 5/28/18.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AccountManagerApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TransactionRestTest {

    TestRestTemplate restTemplate = new TestRestTemplate();

    HttpHeaders headers = new HttpHeaders();

    @LocalServerPort
    private int port;


    @Before
    public void setUp() {
        //add customer
        AddCustomerRequest addCustomerRequest = AddCustomerRequest.builder().name("name").surname("surname").identityNumber("tr").build();
        HttpEntity<AddCustomerRequest> addCustomerRequestHttpEntity = new HttpEntity<>(addCustomerRequest, headers);
        ResponseEntity<AddCustomerResponse> addCustomerResponseResponseEntity = restTemplate.exchange(createURLWithPort("/customer"),
                HttpMethod.POST, addCustomerRequestHttpEntity, AddCustomerResponse.class);

        //add account
        AddAccountRequest addAccountRequest = AddAccountRequest.builder().customerId(1).currency("TRY").build();
        HttpEntity<AddAccountRequest> addCustomerAccountRequestHttpEntity = new HttpEntity<>(addAccountRequest, headers);
        ResponseEntity<AddAccountResponse> addCustomerAccountResponseResponseEntity = restTemplate.exchange(createURLWithPort("/addAccount"),
                HttpMethod.POST, addCustomerAccountRequestHttpEntity, AddAccountResponse.class);
    }

    @Test
    public void transactionRestTest() {
        //makeTransaction
        TransactionRequest transactionRequest = TransactionRequest.builder().accountId(1).amount(10).build();
        HttpEntity<TransactionRequest> transactionRequestHttpEntity = new HttpEntity<>(transactionRequest, headers);
        ResponseEntity<TransactionResponse> transactionResponseResponseEntity = restTemplate.exchange(createURLWithPort("/transaction"), HttpMethod.POST, transactionRequestHttpEntity, TransactionResponse.class);
        Assert.assertEquals(transactionResponseResponseEntity.getStatusCode(), HttpStatus.OK);

        //AccountNotFoundException
        transactionRequest = TransactionRequest.builder().accountId(-1).amount(10).build();
        transactionRequestHttpEntity = new HttpEntity<>(transactionRequest, headers);
        transactionResponseResponseEntity = restTemplate.exchange(createURLWithPort("/transaction"), HttpMethod.POST, transactionRequestHttpEntity, TransactionResponse.class);
        Assert.assertEquals(transactionResponseResponseEntity.getStatusCode(), HttpStatus.NOT_FOUND);

        //InsufficientBalanceException
        transactionRequest = TransactionRequest.builder().accountId(1).amount(-110).build();
        transactionRequestHttpEntity = new HttpEntity<>(transactionRequest, headers);
        transactionResponseResponseEntity = restTemplate.exchange(createURLWithPort("/transaction"), HttpMethod.POST, transactionRequestHttpEntity, TransactionResponse.class);
        Assert.assertEquals(transactionResponseResponseEntity.getStatusCode(), HttpStatus.BAD_REQUEST);


    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }
}
