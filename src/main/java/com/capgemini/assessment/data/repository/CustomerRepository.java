package com.capgemini.assessment.data.repository;

import com.capgemini.assessment.data.entity.Customer;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * Created by silayugurlu on 5/26/18.
 *
 * Repository for customer operations
 */
public interface CustomerRepository extends CrudRepository<Customer, Long> {

    Optional<Customer> findByIdentityNumber(String identityNumber);
}
