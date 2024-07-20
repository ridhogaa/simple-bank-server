package org.k1.simplebankapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
public class LoginResponse {
    private Object accessToken;
    private Object tokenType;
    private Object refreshToken;
    private Object expiresIn;
    private Object scope;
    private Object jti;
}
