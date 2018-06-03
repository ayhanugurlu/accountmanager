package com.capgemini.assessment.service;

import com.capgemini.assessment.data.entity.Customer;
import com.capgemini.assessment.data.repository.CustomerRepository;
import com.capgemini.assessment.service.exception.CustomerAlreadyExistException;
import com.capgemini.assessment.service.exception.CustomerNotFoundException;
import com.capgemini.assessment.service.model.input.customer.AddCustomerInput;
import com.capgemini.assessment.service.model.output.customer.AddCustomerOutput;
import com.capgemini.assessment.service.model.output.customer.GetCustomerOutput;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by silayugurlu on 5/26/18.
 */
@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService {

    @Qualifier("customerServiceMapper")
    @Autowired
    MapperFacade mapperFacade;
    @Autowired
    CustomerRepository repository;
    @Autowired
    private Tracer tracer;

    @Override
    public List<GetCustomerOutput> getAllCustomer() {
        List<GetCustomerOutput> customerOutputs = new ArrayList<>();
        repository.findAll().forEach(customer -> customerOutputs.add(mapperFacade.map(customer, GetCustomerOutput.class)));
        return customerOutputs;
    }

    @Override
    public GetCustomerOutput getCustomer(long id) throws CustomerNotFoundException {
        log.debug("getCustomer method start", tracer.getCurrentSpan().getTraceId());
        Optional<Customer> customer = Optional.ofNullable(repository.findOne(id));
        GetCustomerOutput output = mapperFacade.map(customer.orElseThrow(() -> new CustomerNotFoundException(id)), GetCustomerOutput.class);
        log.debug("getCustomer method finish", tracer.getCurrentSpan().getTraceId());
        return output;
    }

    @Override
    public GetCustomerOutput getCustomerByIdentityNumber(String identityNumber) throws CustomerNotFoundException {
        log.debug("getCustomerByIdentityNumber method start", tracer.getCurrentSpan().getTraceId());
        Optional<Customer> customer = repository.findByIdentityNumber(identityNumber);
        GetCustomerOutput output = mapperFacade.map(customer.orElseThrow(() -> new CustomerNotFoundException(identityNumber)), GetCustomerOutput.class);
        log.debug("getCustomerByIdentityNumber method finish", tracer.getCurrentSpan().getTraceId());
        return output;
    }

    @Override
    public AddCustomerOutput addCustomer(AddCustomerInput addCustomerInput) throws CustomerAlreadyExistException {
        log.debug("addCustomer method start", tracer.getCurrentSpan().getTraceId());
        Optional<Customer> customer = repository.findByIdentityNumber(addCustomerInput.getIdentityNumber());
        if (customer.isPresent()) {
            throw new CustomerAlreadyExistException(addCustomerInput.getIdentityNumber());
        }
        Customer newCust = repository.save(mapperFacade.map(addCustomerInput, Customer.class));
        AddCustomerOutput addCustomerOutput = mapperFacade.map(newCust, AddCustomerOutput.class);
        log.debug("addCustomer method finish", tracer.getCurrentSpan().getTraceId());
        return addCustomerOutput;
    }
}
