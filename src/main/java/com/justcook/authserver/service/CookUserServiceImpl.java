package com.justcook.authserver.service;

import com.justcook.authserver.model.User.CookUser;
import com.justcook.authserver.model.User.UserStatus;
import com.justcook.authserver.model.User.UserRole;
import com.justcook.authserver.repository.CookUserRepository;
import com.justcook.authserver.service.interfaces.CookUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CookUserServiceImpl implements CookUserService, UserDetailsService {

    final CookUserRepository cookUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        CookUser cookUser = cookUserRepository.findByEmail(email);
        if(cookUser == null) {
            log.error("User not found");
            throw new UsernameNotFoundException("User not found");
        } else {
            log.info("User found: " + cookUser.getUsername());
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (UserRole role : cookUser.getUserRoles()) {
            authorities.add(new SimpleGrantedAuthority(role.name()));
        }
        return new org.springframework.security.core.userdetails.User(cookUser.getUsername(), cookUser.getPassword(), authorities);
    }

    @Override
    public CookUser saveUser(CookUser cookUser) {
        cookUser.setUserRoles(List.of(UserRole.USER));
        cookUser.setRegistrationDate(LocalDateTime.now());
        cookUser.setStatus(UserStatus.NEW);
        cookUser.setAllergies(List.of());
        cookUser.setPassword(passwordEncoder.encode(cookUser.getPassword()));
        return cookUserRepository.save(cookUser);
    }

    @Override
    public void giveUserRole(String email, String userRole) {
        CookUser cookUser = cookUserRepository.findByEmail(email);
        if(!cookUser.getUserRoles().contains(UserRole.valueOf(userRole))){
            cookUser.getUserRoles().add(UserRole.valueOf(userRole));
        }
        cookUserRepository.save(cookUser);
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
