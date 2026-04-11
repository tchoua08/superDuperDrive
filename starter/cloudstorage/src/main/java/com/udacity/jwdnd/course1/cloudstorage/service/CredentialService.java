package com.udacity.jwdnd.course1.cloudstorage.service;
import com.udacity.jwdnd.course1.cloudstorage.model.CredentialForm;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CredentialService {

    public List<CredentialForm> getAllCredentials() {
        return new ArrayList<>();
    }

    public void createCredential(CredentialForm credentialForm) {
        System.out.println("Create credential: " + credentialForm.getUrl());
    }

    public void updateCredential(CredentialForm credentialForm) {
        System.out.println("Update credential: " + credentialForm.getCredentialId());
    }

    public void deleteCredential(Integer credentialId) {
        System.out.println("Delete credential: " + credentialId);
    }
}