package org.k1.simplebankapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.k1.simplebankapp.dto.BankTransferRequest;
import org.k1.simplebankapp.dto.BaseResponse;
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

    @PostMapping
    @Operation(summary = "save bank transfer", description = "Endpoint to save bank transfer", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<?> postListTransfer(@RequestBody BankTransferRequest request, Principal principal) {
        return ResponseEntity.ok(BaseResponse.success(bankTransferService.postListTransfer(request, principal), "Success save bank transfer"));
    }

    @GetMapping("{noAccount}")
    @Operation(summary = "get all bank transfer", description = "Endpoint to get all bank transfer", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<?> findAll(Principal principal, @PathVariable String noAccount) {
        return ResponseEntity.ok(BaseResponse.success(bankTransferService.findAll(noAccount, principal), "Success get all bank transfer"));
    }
}
