package com.ror.foodapp.repository;

import com.ror.foodapp.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    User findByEmail(String email);
}

