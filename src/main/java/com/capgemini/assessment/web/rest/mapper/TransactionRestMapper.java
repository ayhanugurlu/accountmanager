package com.capgemini.assessment.web.rest.mapper;

import com.capgemini.assessment.service.model.input.transaction.TransactionInput;
import com.capgemini.assessment.service.model.output.account.AccountTransactionOutput;
import com.capgemini.assessment.web.rest.request.transaction.TransactionRequest;
import com.capgemini.assessment.web.rest.response.transaction.TransactionResponse;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.ConfigurableMapper;
import org.springframework.stereotype.Component;

/**
 * Created by silayugurlu on 5/26/18.
 */
@Component
public class TransactionRestMapper extends ConfigurableMapper {


    protected void configure(MapperFactory factory) {
        factory.classMap(TransactionRequest.class, TransactionInput.class)
                .byDefault()
                .register();

        factory.classMap(AccountTransactionOutput.class, TransactionResponse.class)
                .byDefault()
                .register();

    }


}
