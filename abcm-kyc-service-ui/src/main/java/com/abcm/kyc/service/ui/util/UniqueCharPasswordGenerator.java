package com.abcm.kyc.service.ui.util;

import java.security.SecureRandom;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class UniqueCharPasswordGenerator {

    private static final String CHAR_POOL = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@#$%&!";
    private static final int PASSWORD_LENGTH = 10;
    private static final SecureRandom RANDOM = new SecureRandom();

    /**
     * Generates a secure password with non-repeating characters.
     *
     * @return A secure random password.
     */
    public static String generatePassword() {
        if (PASSWORD_LENGTH > CHAR_POOL.length()) {
            throw new IllegalArgumentException("Password length exceeds unique characters available.");
        }

        List<Character> shuffledChars = CHAR_POOL
                .chars()
                .mapToObj(c -> (char) c)
                .collect(Collectors.collectingAndThen(
                        Collectors.toList(),
                        list -> {
                            Collections.shuffle(list, RANDOM);
                            return list.stream();
                        }
                ))
                .limit(PASSWORD_LENGTH)
                .collect(Collectors.toList());

        return shuffledChars.stream()
                .map(String::valueOf)
                .collect(Collectors.joining());
    }

	/*
	 * public static void main(String[] args) {
	 * System.out.println("Generated Password: " + generatePassword()); }
	 */
}
