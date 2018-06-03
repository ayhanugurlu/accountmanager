package com.capgemini.assessment.service.mapper;

import com.capgemini.assessment.data.entity.Account;
import com.capgemini.assessment.data.entity.Transaction;
import com.capgemini.assessment.service.model.input.account.AddAccountInput;
import com.capgemini.assessment.service.model.output.account.AddAccountOutput;
import com.capgemini.assessment.service.model.output.account.GetAccountOutput;
import com.capgemini.assessment.service.model.output.account.GetTransactionOutput;
import com.capgemini.assessment.service.model.output.account.AccountTransactionOutput;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.ConfigurableMapper;
import org.springframework.stereotype.Component;

/**
 * Created by silayugurlu on 5/26/18.
 *
 * Converts Service Layer Account objects to Data Layer Object
 */
@Component
public class AccountServiceMapper extends ConfigurableMapper {


    protected void configure(MapperFactory factory) {

        factory.classMap(AddAccountInput.class, Account.class)
                .field("customerId", "customer.id")
                .byDefault()
                .register();
        factory.classMap(Account.class, AddAccountOutput.class)
                .field("customer.id", "customerId")
                .byDefault()
                .register();

        factory.classMap(Account.class, GetTransactionOutput.class)
                .field("customer.name", "name")
                .field("customer.surname", "surname")
                .byDefault()
                .register();

        factory.classMap(Transaction.class, AccountTransactionOutput.class).byDefault()
                .register();


        factory.classMap(Account.class, GetAccountOutput.class)
                .field("customer.id", "customerId")
                .byDefault()
                .register();

    }
}
