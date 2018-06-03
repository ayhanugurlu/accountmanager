package com.capgemini.assessment.service.mapper;

import com.capgemini.assessment.data.entity.Customer;
import com.capgemini.assessment.service.model.input.customer.AddCustomerInput;
import com.capgemini.assessment.service.model.output.customer.AddCustomerOutput;
import com.capgemini.assessment.service.model.output.customer.GetCustomerOutput;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.ConfigurableMapper;
import org.springframework.stereotype.Component;

/**
 * Created by silayugurlu on 5/26/18.
 *
 * Converts Service Layer Customer objects to Data Layer objects
 */
@Component
public class CustomerServiceMapper extends ConfigurableMapper {


    protected void configure(MapperFactory factory) {

        factory.classMap(AddCustomerInput.class, Customer.class)
                .byDefault()
                .register();

        factory.classMap(Customer.class, GetCustomerOutput.class)
                .byDefault()
                .register();

        factory.classMap(Customer.class, AddCustomerOutput.class)
                .byDefault()
                .register();
    }
}
