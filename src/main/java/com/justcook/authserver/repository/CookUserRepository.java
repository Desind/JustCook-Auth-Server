package com.justcook.authserver.repository;

import com.justcook.authserver.model.User.CookUser;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CookUserRepository extends MongoRepository<CookUser, String> {
    CookUser findByUsername(String username);
    List<CookUser> findByUsernameContains(String usernameQuery);
    CookUser findByEmail(String email);
}
