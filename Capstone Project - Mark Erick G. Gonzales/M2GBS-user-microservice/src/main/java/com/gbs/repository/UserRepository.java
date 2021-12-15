package com.gbs.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gbs.entity.User;

public interface UserRepository extends JpaRepository<User, String> {

}
