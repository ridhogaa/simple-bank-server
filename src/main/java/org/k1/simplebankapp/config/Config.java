package org.k1.simplebankapp.config;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;
import java.util.UUID;

@Component
public class Config {
    public String convertDateToString(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        return dateFormat.format(date);
    }

    public UUID isValidUUID(String uuid) {
        try {
            return UUID.fromString(uuid);
        } catch (IllegalArgumentException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "UUID is not valid!");
        }
    }

    public static String randomString(int size, boolean numberOnly) {
        String saltChars = "1234567890";
        if (!numberOnly) {
            saltChars += "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        }
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < size) {
            int index = (int) (rnd.nextFloat() * saltChars.length());
            salt.append(saltChars.charAt(index));
        }

        return salt.toString();
    }

    public static String generateTransactionId() {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String formattedDate = currentDate.format(formatter);

        Random random = new Random();
        int randomNumber = random.nextInt(900000) + 100000; // Ensure 6 digits

        return formattedDate + randomNumber;
    }

    public static Date dateNow = new Date();

    public static Date dateOneMonthLater = new Date(dateNow.getTime() + 30 * 24 * 60 * 60 * 1000);

    public static Integer currentMonth = LocalDate.now().getMonthValue();

    public static Boolean isBankBCA(String bankName) {
        return bankName.equals("BCA");
    }

    public static String convertToDateWIB(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Jakarta"));

        return sdf.format(date);
    }

    public static boolean isValidMonth(Integer month) {
        return month >= 1 && month <= 12;
    }

    public static String formatDate(String pattern, Date date) {
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern(pattern);
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().format(myFormatObj);
    }
}
