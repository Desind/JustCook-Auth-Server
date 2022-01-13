package com.justcook.authserver.user;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JWT {
    String access_token;
    String refresh_token;
    String token_type;
    String expires_in;
}
