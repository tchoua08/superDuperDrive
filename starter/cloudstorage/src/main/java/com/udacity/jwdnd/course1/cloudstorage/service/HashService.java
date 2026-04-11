package com.udacity.jwdnd.course1.cloudstorage.service;

import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

@Service
public class HashService {

    public String getHashedValue(String data, String salt) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(salt.getBytes(StandardCharsets.UTF_8));
            byte[] hashedBytes = messageDigest.digest(data.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hashedBytes);
        } catch (Exception e) {
            throw new RuntimeException("Hashing password error", e);
        }
    }
}
