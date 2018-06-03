package com.capgemini.assessment.service;

import com.capgemini.assessment.service.exception.CustomerAlreadyExistException;
import com.capgemini.assessment.service.exception.CustomerNotFoundException;
import com.capgemini.assessment.service.model.input.customer.AddCustomerInput;
import com.capgemini.assessment.service.model.output.customer.AddCustomerOutput;
import com.capgemini.assessment.service.model.output.customer.GetCustomerOutput;

import java.util.List;


/**
 * Created by silayugurlu on 5/26/18.
 */
public interface CustomerService {

    /**
     * Lists all customers
     *
     * @return
     */
    List<GetCustomerOutput> getAllCustomer();

    /**
     * Finds customer by id
     *
     * @param id
     * @return
     * @throws CustomerNotFoundException
     */
    GetCustomerOutput getCustomer(long id) throws CustomerNotFoundException;

    /**
     * Finds customer by identity number
     * @param identityNumber
     * @return
     * @throws CustomerNotFoundException
     */
    GetCustomerOutput getCustomerByIdentityNumber(String identityNumber) throws CustomerNotFoundException;

    /**
     * Adds customer
     *
     * @param addCustomerInput
     * @return
     * @throws CustomerAlreadyExistException
     */
    AddCustomerOutput addCustomer(AddCustomerInput addCustomerInput) throws CustomerAlreadyExistException;
}
