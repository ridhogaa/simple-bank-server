package org.k1.simplebankapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.k1.simplebankapp.dto.BaseResponse;
import org.k1.simplebankapp.dto.RequestNoAccount;
import org.k1.simplebankapp.entity.enums.MutationType;
import org.k1.simplebankapp.service.MutationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;

import java.security.Principal;


@RestController
@RequestMapping("v1/mutations")
public class MutationController {

    @Autowired
    private MutationService mutationService;

    @GetMapping
    @Operation(summary = "Mutation transaction", description = "Endpoint to get mutation transaction", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<?> findAll(
            @RequestParam Integer month,
            @RequestParam(required = false) MutationType type,
            @RequestBody RequestNoAccount noAccount,
            @PageableDefault(page = 0, size = 10) Pageable pageable,
            Principal principal
    ) {
        return ResponseEntity.ok(BaseResponse.success(mutationService.findAllByMonthAndMutationType(month, type, noAccount, pageable, principal), "Success Get All Mutations"));
    }
}
