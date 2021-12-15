package com.gbs.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gbs.entity.Account;

public interface AccountRepository extends JpaRepository<Account, String>{

}
