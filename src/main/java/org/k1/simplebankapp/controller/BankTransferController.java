package org.k1.simplebankapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.k1.simplebankapp.dto.BaseResponse;
import org.k1.simplebankapp.dto.TransactionBankRequest;
import org.k1.simplebankapp.dto.ValidateTransactionRequest;
import org.k1.simplebankapp.service.BankTransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@Tag(name = "Bank Transfer")
@RequestMapping("v1/bank-transfers")
public class BankTransferController {

    @Autowired
    private BankTransferService bankTransferService;

    @PostMapping("create-transaction")
    @Operation(summary = "Create transaction bank transfers", description = "Endpoint to Create transaction bank transfers", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<?> createTransaction(@RequestBody TransactionBankRequest request, Principal principal) {
        return ResponseEntity.ok(BaseResponse.success(bankTransferService.createTransaction(request, principal), "Create transaction success, please validate pin!"));
    }

    @PostMapping("confirm-transfer")
    @Operation(summary = "Confirm transfers", description = "Endpoint to Confirm transfer", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<?> validateTransaction(@RequestBody ValidateTransactionRequest request, Principal principal) {
        return ResponseEntity.ok(BaseResponse.success(bankTransferService.validateTransaction(principal, request), "Create transaction success, please cek history mutation!"));
    }
}
