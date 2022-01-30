package com.justcook.authserver.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.justcook.authserver.dto.CookUserDto;
import com.justcook.authserver.dto.NewUserDto;
import com.justcook.authserver.dto.UserImageDto;
import com.justcook.authserver.dto.UserProfileDto;
import com.justcook.authserver.model.User.CookUser;
import com.justcook.authserver.model.User.UserRole;
import com.justcook.authserver.model.User.UserStatus;
import com.justcook.authserver.repository.CookUserRepository;
import com.justcook.authserver.repository.RecipeRepository;
import com.justcook.authserver.service.interfaces.CookUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Service("cookUserService")
@RequiredArgsConstructor
@Slf4j
public class CookUserServiceImpl implements CookUserService, UserDetailsService {

    final CookUserRepository cookUserRepository;
    final RecipeRepository recipeRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        CookUser cookUser = cookUserRepository.findByEmail(email);
        if(cookUser == null) {
            throw new UsernameNotFoundException("User not found");
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (UserRole role : cookUser.getUserRoles()) {
            authorities.add(new SimpleGrantedAuthority(role.name()));
        }
        return new org.springframework.security.core.userdetails.User(cookUser.getEmail(), cookUser.getPassword(), authorities);
    }

    @Override
    public CookUser saveUser(NewUserDto newUserDto) {
        List<CookUser> cookUsers = cookUserRepository.findAll();
        CookUser cookUser = new CookUser();
        cookUser.setEmail(newUserDto.getEmail());
        cookUser.setUsername(newUserDto.getUsername());
        if(cookUsers.isEmpty()){
            cookUser.setUserRoles(Collections.singletonList(UserRole.ADMIN));
        }else{
            cookUser.setUserRoles(Collections.singletonList(UserRole.USER));
        }
        cookUser.setRegistrationDate(LocalDateTime.now());
        cookUser.setStatus(UserStatus.NEW);
        cookUser.setAllergies(Collections.emptyList());
        cookUser.setPassword(passwordEncoder.encode(newUserDto.getPassword()));
        try {
            return cookUserRepository.save(cookUser);
        }catch (Exception e){
            return null;
        }
    }

    @Override
    public void giveUserRole(String email, UserRole userRole) {
        CookUser cookUser = cookUserRepository.findByEmail(email);
        cookUser.setUserRoles(Collections.singletonList(userRole));
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
    public Optional<CookUser> getCookUserByUsername(String username) {
        return cookUserRepository.findByUsername(username);
    }

    @Override
    public List<CookUser> getCookUsers() {
        return cookUserRepository.findAll();
    }

    @Override
    public void tokenRefresh(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
            try{
                String refreshToken = authorizationHeader.substring("Bearer ".length());
                Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(refreshToken);
                String username = decodedJWT.getSubject();
                CookUser user = getCookUserByEmail(username);
                log.warn("User: {}", user.toString());
                Date date = new Date(System.currentTimeMillis() + 4 * 60 * 60 * 1000);
                String accessToken = JWT.create()
                        .withSubject(user.getUsername())
                        .withExpiresAt(date)
                        .withIssuer(request.getRequestURL().toString())
                        .withClaim("roles", user.getUserRoles().stream().map(UserRole::name).collect(Collectors.toList()))
                        .sign(algorithm);
                Map<String, String> newToken = new HashMap<>();
                newToken.put("access_token", accessToken);
                newToken.put("refresh_token", refreshToken);
                newToken.put("expires_in", String.valueOf(4 * 60 * 60));
                newToken.put("token_type", "Bearer ");
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), newToken);
            }catch(Exception e){
                log.error("Error logging in : {}", e.getMessage());
                response.setHeader("error", e.getMessage());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                Map<String, String> error = new HashMap<>();
                error.put("error_message: ", e.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), error);
            }
        }else{
            throw new RuntimeException("Refresh token is missing");
        }
    }

    @Override
    public void likeRecipe(String email, String id) {
        CookUser cookUser = cookUserRepository.findByEmail(email);
        try{
            if(!cookUser.getFavouriteRecipes().contains(id)){
                cookUser.getFavouriteRecipes().add(id);
                cookUserRepository.save(cookUser);
            }
        }catch(Exception e){
            List<String> likedRecipes = new ArrayList<>();
            likedRecipes.add(id);
            cookUser.setFavouriteRecipes(likedRecipes);
            cookUserRepository.save(cookUser);
        }

    }

    @Override
    public boolean dislikeRecipe(String email, String id) {
        CookUser cookUser = cookUserRepository.findByEmail(email);
        if(cookUser.getFavouriteRecipes().contains(id)){
            cookUser.getFavouriteRecipes().remove(id);
            cookUserRepository.save(cookUser);
            return true;
        }else{
            return false;
        }
    }

    @Override
    public String getUsernameFromId(String id) {
        Optional<CookUser> cookUser = cookUserRepository.findById(id);
        return cookUser.map(CookUser::getUsername).orElse(null);
    }

    @Override
    public List<String> getUserFavouriteRecipes(String id) {
        Optional<CookUser> cookUser = cookUserRepository.findById(id);
        return cookUser.map(CookUser::getFavouriteRecipes).orElse(null);
    }

    @Override
    public List<CookUserDto> findByUsernameContainsAndRole(String username, List<UserRole> role) {
        List<CookUserDto> cookUserDtoList = cookUserRepository.findByUsernameContainsAndUserRolesContaining(username,role);
        for(CookUserDto user : cookUserDtoList){
            user.setRecipesCreated(recipeRepository.countRecipesByOwner(user.getId()));
        }
        return cookUserDtoList;
    }

    @Override
    public void deleteUser(String id) {
        cookUserRepository.deleteById(id);
    }

    @Override
    public UserProfileDto getUserProfile(String id){
        Optional<CookUser> cookUser = Optional.ofNullable(cookUserRepository.findByEmail(id));
        UserProfileDto userProfileDto = null;
        if(cookUser.isPresent()){
            userProfileDto = new UserProfileDto(
                    cookUser.get().getId(),
                    cookUser.get().getUsername(),
                    cookUser.get().getEmail(),
                    cookUser.get().getRegistrationDate(),
                    cookUser.get().getUserRoles(),
                    cookUser.get().getImage(),
                    cookUser.get().getFavouriteRecipes(),
                    cookUser.get().getAllergies());
        }
        return userProfileDto;
    }

    @Override
    public CookUser setUserImage(String email, UserImageDto userImage){
        CookUser cookUser = cookUserRepository.findByEmail(email);
        cookUser.setImage(userImage.getImage());
        return cookUserRepository.save(cookUser);
    }

}
