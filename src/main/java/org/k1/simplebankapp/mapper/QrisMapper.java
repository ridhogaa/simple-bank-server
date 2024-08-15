package org.k1.simplebankapp.mapper;

import org.k1.simplebankapp.dto.QRCodeResponse;
import org.k1.simplebankapp.entity.QrisPayment;
import org.springframework.stereotype.Component;

@Component
public class QrisMapper {

    public QRCodeResponse toQRCodeResponse(QrisPayment qrisPayment) {
        return QRCodeResponse.builder()
                .qrCode(qrisPayment.getQrisCode())
                .build();
    }
}
