package org.k1.simplebankapp.service;

import org.k1.simplebankapp.dto.LoginRequest;
import org.k1.simplebankapp.dto.LoginResponse;

public interface AuthService {
    LoginResponse login(LoginRequest request);

    Object forgotPassword();
}
