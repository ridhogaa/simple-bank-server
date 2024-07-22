package org.k1.simplebankapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.k1.simplebankapp.dto.BaseResponse;
import org.k1.simplebankapp.service.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/banks")
@Tag(name = "Bank")
public class BankController {

    @Autowired
    private BankService bankService;

    @GetMapping
    @Operation(summary = "Get All Banks", description = "Endpoint to Get All Banks", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<?> findAll() {
        return ResponseEntity.ok(BaseResponse.success(bankService.findAll(), "Success Get All Banks"));
    }
}
