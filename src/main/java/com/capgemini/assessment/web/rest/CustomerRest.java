package com.capgemini.assessment.web.rest;

import com.capgemini.assessment.service.CustomerService;
import com.capgemini.assessment.service.exception.CustomerAlreadyExistException;
import com.capgemini.assessment.service.exception.CustomerNotFoundException;
import com.capgemini.assessment.service.model.input.customer.AddCustomerInput;
import com.capgemini.assessment.service.model.output.customer.AddCustomerOutput;
import com.capgemini.assessment.service.model.output.customer.GetCustomerOutput;
import com.capgemini.assessment.web.rest.request.customer.AddCustomerRequest;
import com.capgemini.assessment.web.rest.response.customer.AddCustomerResponse;
import com.capgemini.assessment.web.rest.response.customer.GetCustomerResponse;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.web.bind.annotation.*;

/**
 * Created by silayugurlu on 5/26/18.
 */
@Slf4j
@RestController
public class CustomerRest {

    @Autowired
    CustomerService customerService;
    @Autowired
    @Qualifier("customerRestMapper")
    private MapperFacade mapperFacade;
    @Autowired
    private Tracer tracer;

    @ApiOperation(value = "get customer ",
            notes = "Finds customer by identity number and returns it.<br/>")
    @GetMapping("getCustomer/{id}")
    public @ResponseBody
    GetCustomerResponse getCustomer(@ApiParam(value = "nationality id") @PathVariable(name = "id") String identityNumber) throws CustomerNotFoundException {
        log.debug("getCustomer method start", tracer.getCurrentSpan().getTraceId());
        GetCustomerOutput getCustomerOutput = customerService.getCustomerByIdentityNumber(identityNumber);
        GetCustomerResponse getCustomerResponse = mapperFacade.map(getCustomerOutput, GetCustomerResponse.class);
        log.debug("getCustomer method finish", tracer.getCurrentSpan().getTraceId());
        return getCustomerResponse;
    }

    @ApiOperation(value = "add customer",
            notes = "add customer if does not exist.<br/>")
    @PostMapping("addCustomer")
    public @ResponseBody
    AddCustomerResponse addCustomer(@ApiParam(value = "nationality id, name and surname") @RequestBody AddCustomerRequest addCustomerRequest) throws CustomerAlreadyExistException {
        log.debug("addCustomer method start", tracer.getCurrentSpan().getTraceId());
        AddCustomerInput input = mapperFacade.map(addCustomerRequest, AddCustomerInput.class);
        AddCustomerOutput output = customerService.addCustomer(input);
        AddCustomerResponse customerResponse = mapperFacade.map(output, AddCustomerResponse.class);
        log.debug("addCustomer method finish", tracer.getCurrentSpan().getTraceId());

        return customerResponse;
    }

}
