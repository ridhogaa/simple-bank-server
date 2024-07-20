package org.k1.simplebankapp.config.security;

import lombok.extern.slf4j.Slf4j;
import org.k1.simplebankapp.entity.User;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class CustomTokenEnhancer implements TokenEnhancer {

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        try {
            Map<String, Object> additionalInfo = new HashMap<>();

            User user = getUser(authentication);
            additionalInfo.put("id", user.getId());
            additionalInfo.put("full_name", user.getFullname());

            ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
            return accessToken;
        } catch (Exception e) {
            log.error("Error while enhancing token", e);
            throw e;
        }

    }

    private User getUser(OAuth2Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            return (User) authentication.getPrincipal();
        }
        return null;
    }
}
