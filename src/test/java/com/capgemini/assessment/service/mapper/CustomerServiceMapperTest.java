package com.capgemini.assessment.service.mapper;

import com.capgemini.assessment.AccountManagerApplication;
import com.capgemini.assessment.data.entity.Customer;
import com.capgemini.assessment.service.model.input.customer.AddCustomerInput;
import com.capgemini.assessment.service.model.output.customer.AddCustomerOutput;
import com.capgemini.assessment.service.model.output.customer.GetCustomerOutput;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by Silay.Ugurlu on 28/05/2018
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = AccountManagerApplication.class)
public class CustomerServiceMapperTest {


    @Autowired
    CustomerServiceMapper customerServiceMapper;

    @Test
    public void shouldMapAddCustomerInputToCustomer() {
        AddCustomerInput addCustomerInput = AddCustomerInput.builder().identityNumber("1").name("name").surname("surname").build();
        Customer customer = customerServiceMapper.map(addCustomerInput, Customer.class);
        Assert.assertEquals(customer.getName(), addCustomerInput.getName());
        Assert.assertEquals(customer.getIdentityNumber(), addCustomerInput.getIdentityNumber());
        Assert.assertEquals(customer.getSurname(), addCustomerInput.getSurname());
    }


    @Test
    public void shouldMapCustomerToGetCustomerOutput() {
        Customer customer = Customer.builder().id(1).identityNumber("a").name("name").surname("surname").build();
        GetCustomerOutput getCustomerOutput = customerServiceMapper.map(customer, GetCustomerOutput.class);
        Assert.assertEquals(customer.getName(), getCustomerOutput.getName());
        Assert.assertEquals(customer.getIdentityNumber(), getCustomerOutput.getIdentityNumber());
        Assert.assertEquals(customer.getSurname(), getCustomerOutput.getSurname());
    }

    @Test
    public void shouldMapCustomerToAddCustomerOutput() {
        Customer customer = Customer.builder().id(1).identityNumber("a").name("name").surname("surname").build();
        AddCustomerOutput addCustomerOutput = customerServiceMapper.map(customer, AddCustomerOutput.class);
        Assert.assertEquals(customer.getName(), addCustomerOutput.getName());
        Assert.assertEquals(customer.getIdentityNumber(), addCustomerOutput.getIdentityNumber());
        Assert.assertEquals(customer.getSurname(), addCustomerOutput.getSurname());
    }

}
