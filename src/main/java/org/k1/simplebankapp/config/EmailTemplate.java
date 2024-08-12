package org.k1.simplebankapp.config;

import org.k1.simplebankapp.entity.Transaction;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class EmailTemplate {

    public String createTransactionSuccessEmail(Transaction transaction) {

        return "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<style>" +
                "  body { font-family: Arial, sans-serif; color: #333; }" +
                "  .content { max-width: 600px; margin: auto; padding: 20px; border: 1px solid #ddd; border-radius: 10px; background-color: #f9f9f9; }" +
                "  .header { font-size: 18px; font-weight: bold; }" +
                "  .footer { margin-top: 20px; font-size: 12px; color: #555; }" +
                "  a { color: #0000EE; text-decoration: underline; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class=\"content\">" +
                "  <p>Halo " + transaction.getAccount().getUser().getFullname() + ",</p>" +
                "  <p>Kami ingin menginformasikan bahwa transaksi Anda telah berhasil diproses. Berikut adalah rincian transaksi Anda:</p>" +
                "  <ul>" +
                "    <li><strong>Nomor Rekening Pengirim:</strong> " + transaction.getAccount().getNo() + "</li>" +
                "    <li><strong>Nama Pengirim:</strong> " + transaction.getAccount().getUser().getFullname() + "</li>" +
                "    <li><strong>Nomor Rekening Penerima:</strong> " + transaction.getRecipientTargetAccount() + "</li>" +
                "    <li><strong>Nama Penerima:</strong> " + transaction.getRecipientTargetName() + "</li>" +
                "    <li><strong>Jumlah Transfer:</strong> Rp " + transaction.getAmount() + "</li>" +
                "    <li><strong>Tanggal & Waktu:</strong> " + Config.convertToDateWIB(new Date()) + "</li>" +
                "    <li><strong>Keterangan:</strong> " + (transaction.getDescription() != null ? transaction.getDescription() : "Tidak ada keterangan") + "</li>" +
                "  </ul>" +
                "  <p>Terima kasih telah memilih SimpleBank. Jika Anda memiliki pertanyaan atau memerlukan bantuan lebih lanjut, " +
                "  jangan ragu untuk menghubungi tim customer service kami melalui email <a href=\"mailto:simplebankteams@gmail.com\">simplebankteams@gmail.com</a></p>" +
                "  <p class=\"footer\">Salam hangat,<br/>Tim SimpleBank</p>" +
                "</div>" +
                "</body>" +
                "</html>";
    }
}
