package com.udacity.jwdnd.course1.cloudstorage.service;
import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.springframework.stereotype.Service;

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

    public void addCredential(Credential credential, Integer userId) {
        String key = UUID.randomUUID().toString();
        String encryptedPassword = encryptionService.encryptValue(credential.getPassword(), key);
        credential.setUserId(userId);
        credential.setKey(key);
        credential.setPassword(encryptedPassword);
        credentialMapper.insert(credential);
    }

    public void updateCredential(Credential credential, Integer userId) {
        String key = UUID.randomUUID().toString();
        String encryptedPassword = encryptionService.encryptValue(credential.getPassword(), key);
        credential.setUserId(userId);
        credential.setKey(key);
        credential.setPassword(encryptedPassword);
        credentialMapper.update(credential);
    }

    public String getDecryptedPassword(Credential credential) {
        return encryptionService.decryptValue(credential.getPassword(), credential.getKey());
    }
    public void deleteCredential(Integer credentialId){
        credentialMapper.delete(credentialId);
    }

    public List<Credential> getCredentialsByUserId(Integer userId) {
        return credentialMapper.getCredentialsByUserId(userId);
    }
}
