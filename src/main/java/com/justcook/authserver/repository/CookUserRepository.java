package com.justcook.authserver.repository;

import com.justcook.authserver.dto.CookUserDto;
import com.justcook.authserver.model.User.CookUser;
import com.justcook.authserver.model.User.UserRole;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CookUserRepository extends MongoRepository<CookUser, String> {
    CookUser findByUsername(String username);
    List<CookUserDto> findByUsernameContainsAndUserRolesContaining(String username, List<UserRole> userRoles);
    CookUser findByEmail(String email);
    void deleteById(String id);
}
