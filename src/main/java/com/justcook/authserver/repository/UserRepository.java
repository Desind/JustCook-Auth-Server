package com.justcook.authserver.repository;

import com.justcook.authserver.model.User.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {

}
