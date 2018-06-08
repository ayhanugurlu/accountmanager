package com.capgemini.assessment.web.rest;

import com.capgemini.assessment.AccountManagerApplication;
import com.capgemini.assessment.web.rest.request.account.AddAccountRequest;
import com.capgemini.assessment.web.rest.request.customer.AddCustomerRequest;
import com.capgemini.assessment.web.rest.request.transaction.TransactionRequest;
import com.capgemini.assessment.web.rest.response.account.AddAccountResponse;
import com.capgemini.assessment.web.rest.response.account.GetAccountTransactionResponse;
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
public class AccountRestTest {

    TestRestTemplate restTemplate = new TestRestTemplate();

    HttpHeaders headers = new HttpHeaders();

    @LocalServerPort
    private int port;

    private AddCustomerResponse addCustomerResponse;

    private AddAccountResponse addAccountResponse;

    @Before
    public void setUp() {
        //add customer
        AddCustomerRequest addCustomerRequest = AddCustomerRequest.builder().name("name").surname("surname").identityNumber("xx").build();
        HttpEntity<AddCustomerRequest> addCustomerRequestHttpEntity = new HttpEntity<>(addCustomerRequest, headers);
        ResponseEntity<AddCustomerResponse> addCustomerResponseResponseEntity = restTemplate.exchange(createURLWithPort("/customer"),
                HttpMethod.POST, addCustomerRequestHttpEntity, AddCustomerResponse.class);
        addCustomerResponse = addCustomerResponseResponseEntity.getBody();

        //add account
        AddAccountRequest addAccountRequest = AddAccountRequest.builder().customerId(addCustomerResponse.getCustomerId()).currency("TRY").build();
        HttpEntity<AddAccountRequest> addCustomerAccountRequestHttpEntity = new HttpEntity<>(addAccountRequest, headers);
        ResponseEntity<AddAccountResponse> addCustomerAccountResponseResponseEntity = restTemplate.exchange(createURLWithPort("/account"),
                HttpMethod.POST, addCustomerAccountRequestHttpEntity, AddAccountResponse.class);
        addAccountResponse = addCustomerAccountResponseResponseEntity.getBody();

        //add transaction
        TransactionRequest transactionRequest = TransactionRequest.builder().accountId(addAccountResponse.getId()).amount(10).build();
        HttpEntity<TransactionRequest> transactionRequestHttpEntity = new HttpEntity<>(transactionRequest, headers);
        ResponseEntity<TransactionResponse> transactionResponseResponseEntity = restTemplate.exchange(createURLWithPort("/transaction"), HttpMethod.POST, transactionRequestHttpEntity, TransactionResponse.class);
        System.out.println(transactionResponseResponseEntity);

    }


    @Test
    public void accountRestTest() throws Exception {

        //CustomerNotFoundException
        AddAccountRequest addAccountRequest = AddAccountRequest.builder().customerId(-1).currency("TRY").build();
        HttpEntity<AddAccountRequest> addCustomerAccountRequestHttpEntity = new HttpEntity<>(addAccountRequest, headers);
        ResponseEntity<AddAccountResponse> addCustomerAccountResponseResponseEntity = restTemplate.exchange(createURLWithPort("/account"),
                HttpMethod.POST, addCustomerAccountRequestHttpEntity, AddAccountResponse.class);
        Assert.assertEquals(addCustomerAccountResponseResponseEntity.getStatusCode(), HttpStatus.NOT_FOUND);

        //AddAccount
        addAccountRequest = AddAccountRequest.builder().customerId(addCustomerResponse.getCustomerId()).currency("TRY").build();
        addCustomerAccountRequestHttpEntity = new HttpEntity<>(addAccountRequest, headers);
        addCustomerAccountResponseResponseEntity = restTemplate.exchange(createURLWithPort("/account"),
                HttpMethod.POST, addCustomerAccountRequestHttpEntity, AddAccountResponse.class);
        Assert.assertEquals(addCustomerAccountResponseResponseEntity.getStatusCode(), HttpStatus.OK);
        Assert.assertEquals(addCustomerAccountResponseResponseEntity.getBody().getCustomerId(), addCustomerResponse.getCustomerId());

        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        ResponseEntity<GetAccountTransactionResponse> getAccountTransactionResponseResponseEntity = restTemplate.exchange(createURLWithPort("/account/" + addAccountResponse.getId()),
                HttpMethod.GET, entity, GetAccountTransactionResponse.class);
        Assert.assertEquals(getAccountTransactionResponseResponseEntity.getStatusCode(), HttpStatus.OK);
        Assert.assertEquals(getAccountTransactionResponseResponseEntity.getBody().getName(), "name");
        Assert.assertEquals(getAccountTransactionResponseResponseEntity.getBody().getSurname(), "surname");
        Assert.assertEquals(getAccountTransactionResponseResponseEntity.getBody().getTransactions().size(), 1);
        Assert.assertEquals(getAccountTransactionResponseResponseEntity.getBody().getTransactions().get(0).getAmount(), 10);
    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }
}
