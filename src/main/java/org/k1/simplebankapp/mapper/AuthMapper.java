package org.k1.simplebankapp.mapper;

import org.k1.simplebankapp.dto.LoginResponse;
import org.k1.simplebankapp.dto.ProfileResponse;
import org.k1.simplebankapp.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;

@Component
public class AuthMapper {
    public LoginResponse toLoginResponse(ResponseEntity<Map<Object, Object>> response) {
        return LoginResponse.builder()
                .accessToken(Objects.requireNonNull(response.getBody()).get("access_token"))
                .tokenType(response.getBody().get("token_type"))
                .refreshToken(response.getBody().get("refresh_token"))
                .expiresIn(response.getBody().get("expires_in"))
                .scope(response.getBody().get("scope"))
                .jti(response.getBody().get("jti"))
                .build();
    }

    public ProfileResponse toProfileResponse(User user) {
        return ProfileResponse.builder()
                .username(user.getUsername())
                .phoneNumber(user.getPhoneNumber())
                .email(user.getEmail())
                .build();
    }
}
