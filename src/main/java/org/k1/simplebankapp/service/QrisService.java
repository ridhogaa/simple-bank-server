package org.k1.simplebankapp.service;

import org.k1.simplebankapp.dto.*;

import java.security.Principal;

public interface QrisService {

    QRCodeResponse generateQRCode(QRCodeRequest request, Principal principal);

    ScanQrisResponse scanQRIS(String qrCode);

    QrisResponse confirmQRISReceives(ScanQrisReceiveRequest request, Principal principal);

    QrisResponse confirmQRISMerchant(ScanQrisMerchantRequest request, Principal principal);

    ValidateQRCodeResponse validateQrCode(String qrCode);
}
