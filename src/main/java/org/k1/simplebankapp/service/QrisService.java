package org.k1.simplebankapp.service;

import org.k1.simplebankapp.dto.QRCodeRequest;
import org.k1.simplebankapp.dto.QRCodeResponse;
import org.k1.simplebankapp.dto.ScanQRISRequest;

import java.security.Principal;

public interface QrisService {

    QRCodeResponse generateQRCode(QRCodeRequest request, Principal principal);

    Object scanQRIS(ScanQRISRequest request);
}
