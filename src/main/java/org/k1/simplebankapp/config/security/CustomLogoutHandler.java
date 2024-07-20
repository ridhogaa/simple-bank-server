package org.k1.simplebankapp.config.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Slf4j
public class CustomLogoutHandler implements LogoutHandler {

    @Autowired
    private TokenStore tokenStore;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String token = request.getHeader("Authorization");
        log.info("TOKEN NOW --> {}", token);
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            tokenStore.removeAccessToken(tokenStore.readAccessToken(token));
        }
    }
}
