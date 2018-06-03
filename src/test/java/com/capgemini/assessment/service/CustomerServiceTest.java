package com.capgemini.assessment.service;

import com.capgemini.assessment.data.entity.Customer;
import com.capgemini.assessment.data.repository.CustomerRepository;
import com.capgemini.assessment.listener.ApplicationStartup;
import com.capgemini.assessment.service.exception.CustomerAlreadyExistException;
import com.capgemini.assessment.service.exception.CustomerNotFoundException;
import com.capgemini.assessment.service.exception.ErrorCode;
import com.capgemini.assessment.service.model.input.customer.AddCustomerInput;
import com.capgemini.assessment.service.model.output.customer.AddCustomerOutput;
import com.capgemini.assessment.service.model.output.customer.GetCustomerOutput;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * Created by Silay.Ugurlu on 28/05/2018
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class CustomerServiceTest {

    @Qualifier("accountServiceMapper")
    @Autowired
    MapperFacade mapperFacade;
    @MockBean
    CustomerRepository customerRepository;
    @Autowired
    private CustomerService customerService;
    @MockBean
    private ApplicationStartup applicationStartup;

    @MockBean
    private Tracer tracer;

    @MockBean
    private Span span;


    @Before
    public void setUp() throws Exception {
        when(span.getTraceId()).thenReturn(1l);
        when(tracer.getCurrentSpan()).thenReturn(span);
        Customer customer = Customer.builder().id(2).identityNumber("a").name("name").surname("surname").build();
        when(customerRepository.findOne(2l)).thenReturn(customer);
        when(customerRepository.findOne(3l)).thenReturn(null);
        when(customerRepository.findByIdentityNumber("a")).thenReturn(Optional.of(customer));
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);
        when(customerRepository.findByIdentityNumber("c")).thenReturn(Optional.empty());
        List<Customer> customers = new ArrayList<>();
        customers.add(customer);
        when(customerRepository.findAll()).thenReturn(customers);
    }


    @Test
    public void getCustomerTest() throws CustomerNotFoundException {

        try {
            GetCustomerOutput getCustomerOutput = customerService.getCustomer(3);
        } catch (CustomerNotFoundException customerNotFoundException) {
            Assert.assertEquals(customerNotFoundException.getErrorCode(), ErrorCode.CUSTOMER_NOT_FOUND);

        }
        GetCustomerOutput getCustomerOutput = customerService.getCustomer(2);
        Assert.assertEquals(getCustomerOutput.getId(), 2);
        Assert.assertEquals(getCustomerOutput.getName(), "name");
        Assert.assertEquals(getCustomerOutput.getSurname(), "surname");
        Assert.assertEquals(getCustomerOutput.getIdentityNumber(), "a");
    }

    @Test
    public void getCustomerByIdentityNumberTest() throws CustomerNotFoundException {

        try {
            GetCustomerOutput getCustomerOutput = customerService.getCustomerByIdentityNumber("c");
        } catch (CustomerNotFoundException customerNotFoundException) {
            Assert.assertEquals(customerNotFoundException.getErrorCode(), ErrorCode.CUSTOMER_NOT_FOUND);
        }
        GetCustomerOutput getCustomerOutput = customerService.getCustomerByIdentityNumber("a");
        Assert.assertEquals(getCustomerOutput.getId(), 2);
        Assert.assertEquals(getCustomerOutput.getName(), "name");
        Assert.assertEquals(getCustomerOutput.getSurname(), "surname");
        Assert.assertEquals(getCustomerOutput.getIdentityNumber(), "a");
    }


    @Test
    public void addCustomerTest() throws CustomerAlreadyExistException {
        AddCustomerInput addCustomerInput = AddCustomerInput.builder().name("name").surname("surname").identityNumber("a").build();
        try {
            AddCustomerOutput addCustomerOutput = customerService.addCustomer(addCustomerInput);
        } catch (CustomerAlreadyExistException customerAlreadyExistException) {
            Assert.assertEquals(customerAlreadyExistException.getErrorCode(), ErrorCode.CUSTOMER_ALREADY_EXIST);
        }
        addCustomerInput = AddCustomerInput.builder().name("name").surname("surname").identityNumber("c").build();
        AddCustomerOutput addCustomerOutput = customerService.addCustomer(addCustomerInput);
        Assert.assertEquals(addCustomerOutput.getId(), 2);
        Assert.assertEquals(addCustomerOutput.getName(), "name");
        Assert.assertEquals(addCustomerOutput.getSurname(), "surname");
        Assert.assertEquals(addCustomerOutput.getIdentityNumber(), "a");


    }

    @Test
    public void getAllCustomerTest() {
        List<GetCustomerOutput> customerOutputs = customerService.getAllCustomer();
        Assert.assertEquals(customerOutputs.size(), 1);
    }

}
