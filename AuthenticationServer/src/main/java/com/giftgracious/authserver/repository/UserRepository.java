package com.giftgracious.authserver.repository;

import com.giftgracious.authserver.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User,String> {

    Optional<User> findByUsername(String username);

    Optional<User> findById(String id);
    boolean existsByUsername(String username);

}
