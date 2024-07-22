package org.k1.simplebankapp.serviceimpl;

import lombok.extern.slf4j.Slf4j;
import org.k1.simplebankapp.dto.ProfileResponse;
import org.k1.simplebankapp.entity.User;
import org.k1.simplebankapp.mapper.AuthMapper;
import org.k1.simplebankapp.repository.UserRepository;
import org.k1.simplebankapp.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@Service
@Slf4j
public class ProfileServiceImpl implements ProfileService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthMapper authMapper;

    @Override
    public ProfileResponse getProfile(Principal principal) {
        User user = userRepository.findByUsername(principal.getName());
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!");
        }
        return authMapper.toProfileResponse(user);
    }
}
