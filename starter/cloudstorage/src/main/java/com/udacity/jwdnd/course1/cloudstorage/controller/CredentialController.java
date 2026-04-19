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
            int result;
            if (credential.getCredentialId() == null) {
                result = credentialService.addCredential(credential, user.getUserId());
                if (result > 0) {
                    redirectAttributes.addFlashAttribute("successMessage", "Credential created successfully.");
                } else {
                    redirectAttributes.addFlashAttribute("errorMessage", "Unable to create credential.");
                }
            } else {
                credentialService.updateCredential(credential, user.getUserId());
                result = credentialService.updateCredential(credential, user.getUserId());
                if (result > 0) {
                    redirectAttributes.addFlashAttribute("successMessage", "Credential updated successfully.");
                } else {
                    redirectAttributes.addFlashAttribute("errorMessage", "Unable to update credential.");
                }
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Unexpected error while saving credential.");
        }

        return "redirect:/result";
    }

    @GetMapping("/credential/delete/{credentialId}")
    public String deleteCredential(@PathVariable Integer credentialId,
                                   Authentication authentication,
                                   RedirectAttributes redirectAttributes) {
        User user = userService.getUser(authentication.getName());
        try {
            int result = credentialService.deleteCredential(credentialId, user.getUserId());
            if (result > 0) {
                redirectAttributes.addFlashAttribute("successMessage", "Credential deleted successfully.");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Unable to delete credential.");
            }

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Unexpected error while deleting credential.");
        }

        return "redirect:/result";
    }
}
