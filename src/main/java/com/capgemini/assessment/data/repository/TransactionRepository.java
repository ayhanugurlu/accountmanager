package com.capgemini.assessment.data.repository;

import com.capgemini.assessment.data.entity.Transaction;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by silayugurlu on 5/27/18.
 *
 * Repository for transaction operations
 *
 */
public interface TransactionRepository extends CrudRepository<Transaction, Long> {
}
