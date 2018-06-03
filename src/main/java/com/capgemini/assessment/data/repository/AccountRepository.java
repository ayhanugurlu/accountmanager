package com.capgemini.assessment.data.repository;

import com.capgemini.assessment.data.entity.Account;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by silayugurlu on 5/26/18.
 *
 * Repository for account operations
 */
public interface AccountRepository extends CrudRepository<Account, Long> {

    List<Account> findAccountsByCustomer_Id(long customerId);
}
