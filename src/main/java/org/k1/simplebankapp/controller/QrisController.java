package org.k1.simplebankapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.k1.simplebankapp.dto.BaseResponse;
import org.k1.simplebankapp.dto.QRCodeRequest;
import org.k1.simplebankapp.dto.ScanQrisMerchantRequest;
import org.k1.simplebankapp.dto.ScanQrisReceiveRequest;
import org.k1.simplebankapp.service.QrisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("scan-qris/{qrCode}")
    @Operation(summary = "Scan QRIS", description = "Endpoint to Scan QRIS", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<?> scanQRIS(@PathVariable String qrCode) {
        return ResponseEntity.ok(BaseResponse.success(qrisService.scanQRIS(qrCode), "Scan QRIS success"));
    }

    @PostMapping("confirm-qris-receives")
    @Operation(summary = "Confirm QRIS receives funds", description = "Endpoint to Confirm QRIS receives funds", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<?> confirmQRISReceives(@RequestBody ScanQrisReceiveRequest request, Principal principal) {
        return ResponseEntity.ok(BaseResponse.success(qrisService.confirmQRISReceives(request, principal), "Confirm QRIS receives funds success"));
    }

    @PostMapping("confirm-qris-merchant")
    @Operation(summary = "Confirm QRIS Merchants", description = "Endpoint to Confirm QRIS Merchants", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<?> confirmQRISMerchant(@RequestBody ScanQrisMerchantRequest request, Principal principal) {
        return ResponseEntity.ok(BaseResponse.success(qrisService.confirmQRISMerchant(request, principal), "Confirm QRIS Merchants success"));
    }

    @GetMapping("validate-qr-code/{qrCode}")
    @Operation(summary = "Get QRIS Status", description = "Endpoint to Get QRIS Status", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<?> validateQrCode(@PathVariable String qrCode) {
        return ResponseEntity.ok(BaseResponse.success(qrisService.validateQrCode(qrCode), "Get QRIS Status success"));
    }
}
