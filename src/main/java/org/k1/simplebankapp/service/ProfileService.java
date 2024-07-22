package org.k1.simplebankapp.service;

import org.k1.simplebankapp.dto.ProfileResponse;

import java.security.Principal;

public interface ProfileService {
    ProfileResponse getProfile(Principal principal);
}
