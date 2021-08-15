package com.justcook.authserver.service;

import com.justcook.authserver.model.User.CookUser;
import com.justcook.authserver.model.User.UserStatus;
import com.justcook.authserver.model.User.UserType;
import com.justcook.authserver.repository.CookUserRepository;
import com.justcook.authserver.service.interfaces.CookUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CookUserServiceImpl implements CookUserService {

    final CookUserRepository cookUserRepository;

    @Override
    public CookUser saveUser(CookUser cookUser) {
        cookUser.setUserType(UserType.USER);
        cookUser.setRegistrationDate(LocalDateTime.now());
        cookUser.setStatus(UserStatus.NEW);
        cookUser.setAllergies(List.of());
        return cookUserRepository.save(cookUser);
    }

    @Override
    public void setUserType(String email, UserType userType) {
        CookUser cookUser = cookUserRepository.findByEmail(email);
        cookUser.setUserType(userType);
    }

    @Override
    public Optional<CookUser> getCookUserById(String id) {
        return cookUserRepository.findById(id);
    }

    @Override
    public CookUser getCookUserByEmail(String email) {
        return cookUserRepository.findByEmail(email);
    }

    @Override
    public CookUser getCookUserByUsername(String username) {
        return cookUserRepository.findByUsername(username);
    }

    @Override
    public List<CookUser> getCookUsers() {
        return cookUserRepository.findAll();
    }
}
