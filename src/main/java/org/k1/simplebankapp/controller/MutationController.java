package org.k1.simplebankapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.k1.simplebankapp.dto.BaseResponse;
import org.k1.simplebankapp.dto.MutationResponse;
import org.k1.simplebankapp.dto.PagingResponse;
import org.k1.simplebankapp.entity.enums.MutationType;
import org.k1.simplebankapp.service.MutationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;

import java.security.Principal;


@RestController
@RequestMapping("v1/mutations")
@Tag(name = "Mutation")
public class MutationController {

    @Autowired
    private MutationService mutationService;

    @GetMapping("{noAccount}")
    @Operation(summary = "Mutation transaction", description = "Endpoint to get mutation transaction", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<?> findAll(
            @RequestParam Integer month,
            @RequestParam(required = false) MutationType type,
            @PathVariable String noAccount,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Principal principal
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<MutationResponse> mutationResponses = mutationService.findAllByMonthAndMutationType(month, type, noAccount, pageable, principal);
        PagingResponse<Object> pagingResponse = PagingResponse.builder()
                .currentPage(mutationResponses.getNumber())
                .totalPage(mutationResponses.getTotalPages())
                .totalItem((int) mutationResponses.getTotalElements())
                .size(mutationResponses.getSize())
                .pagingData(mutationResponses.getContent())
                .build();
        return ResponseEntity.ok(BaseResponse.success(pagingResponse, "Success Get All Mutations"));
    }

    @GetMapping("{noAccount}/amounts")
    @Operation(summary = "Get amounts", description = "Endpoint to get income", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<?> getAmounts(
            @PathVariable String noAccount,
            Principal principal
    ) {
        return ResponseEntity.ok(BaseResponse.success(mutationService.getSpendingAndIncome(principal, noAccount), "Success Get Mutation amounts"));
    }
}
