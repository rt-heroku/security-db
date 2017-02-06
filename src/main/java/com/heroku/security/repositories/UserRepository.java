package com.heroku.security.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.heroku.security.entities.UserAccount;

@Repository
public interface UserRepository extends CrudRepository<UserAccount, Long> {
    public UserAccount findByUsername(String username);
    
}