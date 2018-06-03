package com.capgemini.assessment.listener;

import com.capgemini.assessment.service.CustomerService;
import com.capgemini.assessment.service.exception.CustomerAlreadyExistException;
import com.capgemini.assessment.service.model.input.customer.AddCustomerInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * Created by silayugurlu on 5/30/18.
 *
 * This class listens application startup
 */
@Component
public class ApplicationStartup
        implements ApplicationListener<ApplicationReadyEvent> {

    private static Logger logger = LoggerFactory.getLogger(ApplicationStartup.class);
    @Autowired
    CustomerService customerService;
    @Autowired
    private Tracer tracer;

    /**
     * When the application is ready, adds initial customers.
     * @param applicationReadyEvent
     */
    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        tracer.createSpan("init");
        AddCustomerInput addCustomerInput = AddCustomerInput.builder().name("name").surname("surname").identityNumber("tr").build();
        try {
            customerService.addCustomer(addCustomerInput);
        } catch (CustomerAlreadyExistException customerAlreadyExistException) {
            logger.error(customerAlreadyExistException.getMessage());
        }
    }
}
