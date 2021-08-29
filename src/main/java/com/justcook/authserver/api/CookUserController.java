package com.justcook.authserver.api;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.justcook.authserver.model.User.CookUser;
import com.justcook.authserver.model.User.RoleToUserForm;
import com.justcook.authserver.model.User.UserRole;
import com.justcook.authserver.service.interfaces.CookUserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@RequestMapping("/api/user")
@AllArgsConstructor
@Slf4j
public class CookUserController {
    public final CookUserService cookUserService;

    @PostMapping("/new")
    public ResponseEntity<CookUser> saveNewCookUser(@RequestBody CookUser cookUser){
        cookUserService.saveUser(cookUser);
        return ResponseEntity.status(201).build();
    }

    @GetMapping("/all")
    public ResponseEntity<List<CookUser>> getAllCookUsers(HttpServletRequest request){
        //PRZYKŁADOWE ODEBRANIE INFORMACJI O UŻYTKOWNIKU Z TOKENA
        String username = String.valueOf(request.getAttribute("username"));
        log.warn("Username from token: " + username);
        List<CookUser> cookUsers = cookUserService.getCookUsers();
        return ResponseEntity.status(200).body(cookUsers);
    }

    @PostMapping("/role/add")
    public ResponseEntity<?> giveCookUserRole(@RequestBody RoleToUserForm form){
        log.info(form.toString());
        cookUserService.giveUserRole(form.getEmail(), form.getUserRole());
        return ResponseEntity.status(200).build();
    }

    @GetMapping("/refreshtoken")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
            try{
                String refreshToken = authorizationHeader.substring("Bearer ".length());
                Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(refreshToken);
                String username = decodedJWT.getSubject();
                CookUser user = cookUserService.getCookUserByEmail(username);
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
}
