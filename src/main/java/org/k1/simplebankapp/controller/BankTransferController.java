package org.k1.simplebankapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.k1.simplebankapp.dto.BankTransferResponse;
import org.k1.simplebankapp.dto.BaseResponse;
import org.k1.simplebankapp.dto.BankTransferRequest;
import org.k1.simplebankapp.dto.ValidateTransactionRequest;
import org.k1.simplebankapp.service.BankTransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@Tag(name = "Bank Transfer")
@RequestMapping("v1/bank-transfers")
public class BankTransferController {

    @Autowired
    private BankTransferService bankTransferService;

    @PostMapping
    @Operation(summary = "Create transaction for bank transfers", description = "Endpoint to Create transaction bank transfers", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = BankTransferResponse.class), mediaType = "application/json")})
    })
    public ResponseEntity<?> createTransaction(@RequestBody BankTransferRequest request, Principal principal) {
        return ResponseEntity.ok(BaseResponse.success(bankTransferService.createTransaction(request, principal), "Create transaction success"));
    }
}
