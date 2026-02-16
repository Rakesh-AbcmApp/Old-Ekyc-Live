package com.abcm.voterId.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Component;

@Component
public class CommonUtils {

    public static long generateUniqueId() {

       Date currentDate = new Date();
       SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

       String formattedDate = dateFormat.format(currentDate);

       long uniqueId = Long.parseLong(formattedDate);

       uniqueId %= 100000000;

       return uniqueId;
    }

    public boolean isNullOrEmpty(String value) {
       return value == null || value.trim().isEmpty();
    }

    public boolean isInvalidUserId(long userId) {
       return userId <= 0; // Assuming valid userId must be greater than 0
    }
    
    public static String readUsingFileInputStream(String fileName) throws IOException {
        FileInputStream fis = null;
        byte[] buffer = new byte[10];
        StringBuilder sb = new StringBuilder();
        try {
            fis = new FileInputStream(fileName);

            while (fis.read(buffer) != -1) {
                sb.append(new String(buffer));
                buffer = new byte[10];
            }
            fis.close();

        } catch (FileNotFoundException e) {
            e.toString();
        } finally {
            if (fis != null)
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        return sb.toString();
    }

}