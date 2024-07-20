package org.k1.simplebankapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.k1.simplebankapp.dto.BaseResponse;
import org.k1.simplebankapp.dto.TransactionBankRequest;
import org.k1.simplebankapp.dto.ValidateTransactionRequest;
import org.k1.simplebankapp.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("v1/transactions")
@Tag(name = "Transaction")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping("bank")
    @Operation(summary = "Create transaction", description = "Endpoint to create transaction", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<?> createTransaction(@RequestBody TransactionBankRequest request, Principal principal) {
        return ResponseEntity.ok(BaseResponse.success(transactionService.createTransaction(request, principal), "Create transaction success, please validate pin!"));
    }

    @PostMapping("validate")
    @Operation(summary = "Validate transaction", description = "Endpoint to validate transaction", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<?> validateTransaction(@RequestBody ValidateTransactionRequest request, Principal principal) {
        return ResponseEntity.ok(BaseResponse.success(transactionService.validateTransaction(principal, request), "Create transaction success, please cek history mutation!"));
    }

}
