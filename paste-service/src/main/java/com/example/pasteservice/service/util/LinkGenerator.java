package com.example.pasteservice.service.util;

import java.security.SecureRandom;
import java.util.Base64;

public class LinkGenerator {

    private static final SecureRandom random = new SecureRandom();
    private static final int LINK_LENGTH = 10;

    public static String generateRandomLink() {
        byte[] randomBytes = new byte[LINK_LENGTH];
        random.nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }
}
