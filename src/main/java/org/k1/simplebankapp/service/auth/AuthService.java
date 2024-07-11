package org.k1.simplebankapp.service.auth;

import org.k1.simplebankapp.dto.auth.LoginRequest;
import org.k1.simplebankapp.dto.auth.LoginResponse;

public interface AuthService {
    LoginResponse login(LoginRequest request);
}
