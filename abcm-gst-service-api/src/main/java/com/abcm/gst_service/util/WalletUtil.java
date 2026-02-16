package com.abcm.gst_service.util;

public class WalletUtil {

    public static void checkEightyPercentUsed(double balance) {
        // Calculate 20% of balance
        double twentyPercentOfBalance = balance * 0.2;

        if (balance <= twentyPercentOfBalance) {
            System.out.println("⚠️  Alert: 80% ya usse zyada balance use ho gaya hai.");
        } else {
            System.out.println("✅ Balance abhi 80% se kam use hua hai.");
        }
    }

    public static void main(String[] args) {
        double balance = 800;
        checkEightyPercentUsed(balance);
    }
}
