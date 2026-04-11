package com.udacity.jwdnd.course1.cloudstorage.controller;
import com.udacity.jwdnd.course1.cloudstorage.model.CredentialForm;
import com.udacity.jwdnd.course1.cloudstorage.service.CredentialService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CredentialController {

    private final CredentialService credentialService;

    public CredentialController(CredentialService credentialService) {
        this.credentialService = credentialService;
    }

    @PostMapping("/credentials")
    public String saveCredential(@ModelAttribute("credentialForm") CredentialForm credentialForm) {
        if (credentialForm.getCredentialId() == null) {
            credentialService.createCredential(credentialForm);
        } else {
            credentialService.updateCredential(credentialForm);
        }

        return "redirect:/home";
    }

    @GetMapping("/credentials/delete")
    public String deleteCredential(@RequestParam("credentialId") Integer credentialId) {
        credentialService.deleteCredential(credentialId);
        return "redirect:/home";
    }
}