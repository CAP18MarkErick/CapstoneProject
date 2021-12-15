package com.gbs.repository;

import org.springframework.data.jpa.repository.JpaRepository;


import com.gbs.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, String> {

}
