package com.capgemini.assessment.web.rest.mapper;

import com.capgemini.assessment.service.model.input.account.AddAccountInput;
import com.capgemini.assessment.service.model.output.account.AddAccountOutput;
import com.capgemini.assessment.service.model.output.account.GetTransactionOutput;
import com.capgemini.assessment.service.model.output.account.AccountTransactionOutput;
import com.capgemini.assessment.web.rest.request.account.AddAccountRequest;
import com.capgemini.assessment.web.rest.response.account.AddAccountResponse;
import com.capgemini.assessment.web.rest.response.account.GetAccountTransactionResponse;
import com.capgemini.assessment.web.rest.response.account.TransactionResponse;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.ConfigurableMapper;
import org.springframework.stereotype.Component;

/**
 * Created by silayugurlu on 5/26/18.
 */
@Component
public class AccountRestMapper extends ConfigurableMapper {


    protected void configure(MapperFactory factory) {
        factory.classMap(AddAccountRequest.class, AddAccountInput.class)
                .byDefault()
                .register();

        factory.classMap(AddAccountOutput.class, AddAccountResponse.class)
                .byDefault()
                .register();

        factory.classMap(GetTransactionOutput.class, GetAccountTransactionResponse.class)
                .byDefault()
                .register();

        factory.classMap(AccountTransactionOutput.class, TransactionResponse.class).byDefault()
                .register();
    }


}
