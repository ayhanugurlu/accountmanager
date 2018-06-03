package com.capgemini.assessment.service.mapper;

import com.capgemini.assessment.data.entity.Transaction;
import com.capgemini.assessment.service.model.input.transaction.TransactionInput;
import com.capgemini.assessment.service.model.output.transaction.TransactionOutput;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.ConfigurableMapper;
import org.springframework.stereotype.Component;

/**
 * Created by silayugurlu on 5/26/18.
 *
 * Converts Service Layer Transaction objects to Data Layer objects
 */
@Component
public class TransactionServiceMapper extends ConfigurableMapper {


    protected void configure(MapperFactory factory) {

        factory.classMap(TransactionInput.class, Transaction.class)
                .field("accountId", "account.id")
                .byDefault()
                .register();
        factory.classMap(Transaction.class, TransactionOutput.class)
                .byDefault()
                .register();


    }
}
