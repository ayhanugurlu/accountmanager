package com.capgemini.assessment.web.rest;

import com.capgemini.assessment.AccountManagerApplication;
import com.capgemini.assessment.web.rest.request.customer.AddCustomerRequest;
import com.capgemini.assessment.web.rest.response.customer.AddCustomerResponse;
import com.capgemini.assessment.web.rest.response.customer.GetCustomerResponse;
import org.junit.Assert;
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
public class CustomerRestTest {

    TestRestTemplate restTemplate = new TestRestTemplate();

    HttpHeaders headers = new HttpHeaders();

    @LocalServerPort
    private int port;


    @Test
    public void customerRestTest() throws Exception {
        //customer not found
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        ResponseEntity<GetCustomerResponse> getCustomerResponseResponseEntity = restTemplate.exchange(createURLWithPort("/customer/1"),
                HttpMethod.GET, entity, GetCustomerResponse.class);
        Assert.assertEquals(getCustomerResponseResponseEntity.getStatusCode(), HttpStatus.NOT_FOUND);

        //add customer
        AddCustomerRequest addCustomerRequest = AddCustomerRequest.builder().name("name").surname("surname").identityNumber("12").build();
        HttpEntity<AddCustomerRequest> addCustomerRequestHttpEntity = new HttpEntity<>(addCustomerRequest, headers);
        ResponseEntity<AddCustomerResponse> addCustomerResponseResponseEntity = restTemplate.exchange(createURLWithPort("/customer"),
                HttpMethod.POST, addCustomerRequestHttpEntity, AddCustomerResponse.class);
        Assert.assertEquals(addCustomerResponseResponseEntity.getStatusCode(), HttpStatus.OK);


        getCustomerResponseResponseEntity = restTemplate.exchange(createURLWithPort("/customer/12"),
                HttpMethod.GET, entity, GetCustomerResponse.class);
        Assert.assertEquals(getCustomerResponseResponseEntity.getStatusCode(), HttpStatus.OK);
        Assert.assertEquals(addCustomerResponseResponseEntity.getBody().getCustomerId(), getCustomerResponseResponseEntity.getBody().getId());

        //duplicate customer
        ResponseEntity<AddCustomerResponse> duplicateResponse = restTemplate.exchange(createURLWithPort("/customer"),
                HttpMethod.POST, addCustomerRequestHttpEntity, AddCustomerResponse.class);
        Assert.assertEquals(duplicateResponse.getStatusCode(), HttpStatus.CONFLICT);

        ///get customer
        getCustomerResponseResponseEntity = restTemplate.exchange(createURLWithPort("/customer/12"),
                HttpMethod.GET, entity, GetCustomerResponse.class);
        Assert.assertEquals(getCustomerResponseResponseEntity.getStatusCode(), HttpStatus.OK);
        Assert.assertEquals(getCustomerResponseResponseEntity.getBody().getName(), "name");
        Assert.assertEquals(getCustomerResponseResponseEntity.getBody().getSurname(), "surname");
        Assert.assertEquals(addCustomerResponseResponseEntity.getBody().getCustomerId(), getCustomerResponseResponseEntity.getBody().getId());

    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }
}
