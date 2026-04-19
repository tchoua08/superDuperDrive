package com.udacity.jwdnd.course1.cloudstorage.service;
import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Service
public class CredentialService {
    private final CredentialMapper credentialMapper;
    private final EncryptionService encryptionService;

    public CredentialService(CredentialMapper credentialMapper, EncryptionService encryptionService) {
        this.credentialMapper = credentialMapper;
        this.encryptionService = encryptionService;
    }

    public int addCredential(Credential credential, Integer userId) {

        String encodedKey = generateEncodedKey();
        String encryptedPassword = encryptionService.encryptValue(credential.getPassword(), encodedKey);
        credential.setUserId(userId);
        credential.setKey(encodedKey);
        credential.setPassword(encryptedPassword);
       return credentialMapper.insert(credential);
    }

    public int updateCredential(Credential credential, Integer userId) {

        Credential existingCredential = credentialMapper.getCredentialByIdAndUserId(credential.getCredentialId(), userId);
        if (existingCredential == null) {
            return 0;
        }
        String encodedKey = generateEncodedKey();
        String encryptedPassword = encryptionService.encryptValue(credential.getPassword(), encodedKey);
        credential.setUserId(userId);
        credential.setKey(encodedKey);
        credential.setPassword(encryptedPassword);
       return credentialMapper.update(credential);
    }

    public String getDecryptedPassword(Credential credential) {
        return encryptionService.decryptValue(credential.getPassword(), credential.getKey());
    }
    public int deleteCredential(Integer credentialId, Integer userId){
        return credentialMapper.delete(credentialId, userId);
    }

    public List<Credential> getCredentialsByUserId(Integer userId) {
        List<Credential> credentials = credentialMapper.getCredentialsByUserId(userId);
        credentials.forEach(credential ->
                credential.setDecryptedPassword(encryptionService.decryptValue(credential.getPassword(), credential.getKey())));
        return credentials;
    }

    private String generateEncodedKey() {
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        return Base64.getEncoder().encodeToString(key);
    }
}
