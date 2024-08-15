package org.k1.simplebankapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.k1.simplebankapp.dto.BaseResponse;
import org.k1.simplebankapp.dto.QRCodeRequest;
import org.k1.simplebankapp.dto.ScanQRISRequest;
import org.k1.simplebankapp.service.QrisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@Tag(name = "Qris")
@RequestMapping("v1/qris")
public class QrisController {

    @Autowired
    private QrisService qrisService;

    @PostMapping("generate-qr-code")
    @Operation(summary = "Create QR Code", description = "Endpoint to Create QR Code", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<?> generateQRCode(@RequestBody QRCodeRequest request, Principal principal) {
        return ResponseEntity.ok(BaseResponse.success(qrisService.generateQRCode(request, principal), "Create QR code success"));
    }

    @PostMapping("scan-qris")
    @Operation(summary = "Create Scan QRIS", description = "Endpoint to Scan QRIS", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<?> scanQRIS(@RequestBody ScanQRISRequest request, Principal principal) {
        return ResponseEntity.ok(BaseResponse.success(qrisService.scanQRIS(request), "Scan QRIS success"));
    }
}
