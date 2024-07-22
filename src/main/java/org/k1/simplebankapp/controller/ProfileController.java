package org.k1.simplebankapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.k1.simplebankapp.dto.BaseResponse;
import org.k1.simplebankapp.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@Tag(name = "Profile")
@RestController
@RequestMapping("v1/profiles")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @GetMapping
    @Operation(summary = "Profile user", description = "Endpoint to get profile user", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<?> getProfile(Principal principal) {
        return ResponseEntity.ok(BaseResponse.success(profileService.getProfile(principal), "Success Get Profile"));
    }
}
