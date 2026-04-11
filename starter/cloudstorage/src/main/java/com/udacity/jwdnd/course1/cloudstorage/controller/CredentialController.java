package com.udacity.jwdnd.course1.cloudstorage.controller;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.service.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CredentialController {

    private final CredentialService credentialService;
    private final UserService userService;

    public CredentialController(CredentialService credentialService, UserService userService) {
        this.credentialService = credentialService;
        this.userService = userService;
    }

    @PostMapping("/credential")
    public String addOrUpdateCredential(Authentication authentication,
                                        Credential credential,
                                        RedirectAttributes redirectAttributes) {

        String username = authentication.getName();
        User user = userService.getUser(username);

        try {
            if (credential.getCredentialId() == null) {
                credentialService.addCredential(credential, user.getUserId());
            } else {
                credentialService.updateCredential(credential, user.getUserId());
            }
            redirectAttributes.addFlashAttribute("success", true);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", true);
        }

        return "redirect:/result";
    }

    @GetMapping("/credential/delete/{credentialId}")
    public String deleteCredential(@PathVariable Integer credentialId,
                                   RedirectAttributes redirectAttributes) {
        try {
            credentialService.deleteCredential(credentialId);
            redirectAttributes.addFlashAttribute("success", true);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", true);
        }

        return "redirect:/result";
    }
}
