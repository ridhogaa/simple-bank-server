package org.k1.simplebankapp.config.security;

import org.k1.simplebankapp.serviceimpl.Oauth2ClientDetailServiceImpl;
import org.k1.simplebankapp.serviceimpl.Oauth2UserDetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableAuthorizationServer
public class Oauth2AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private Oauth2ClientDetailServiceImpl clientDetailsService;

    @Autowired
    private Oauth2UserDetailServiceImpl userDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AccessTokenConverter accessTokenConverter;

    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private CustomTokenEnhancer customTokenEnhancer;

    /**
     * Change server config, password encoder etc.
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer server) throws Exception {
        server.allowFormAuthenticationForClients()
                .passwordEncoder(passwordEncoder)
        ;
    }

    /**
     * Change client details etc.
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(clientDetailsService);
    }

    /**
     * Change user details etc.
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManager)
                .tokenStore(tokenStore)
                .accessTokenConverter(accessTokenConverter)
                .userDetailsService(userDetailsService)
                .tokenEnhancer(tokenEnhancerChain())
        ;
    }

    @Bean
    public TokenEnhancerChain tokenEnhancerChain() {
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        List<TokenEnhancer> enhancers = new ArrayList<>();

        enhancers.add(customTokenEnhancer);
        enhancers.add((TokenEnhancer) accessTokenConverter);

        tokenEnhancerChain.setTokenEnhancers(enhancers);
        return tokenEnhancerChain;
    }
}

