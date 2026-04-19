package com.udacity.jwdnd.course1.cloudstorage.controller;
import com.udacity.jwdnd.course1.cloudstorage.model.SignupForm;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class SignupController {

    private final UserService userService;

    public SignupController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/signup")
    public String getSignupPage(Model model) {
        model.addAttribute("signupForm", new SignupForm());
        return "signup";
    }

    @PostMapping("/signup")
    public String signupUser(@ModelAttribute("signupForm") SignupForm signupForm,
                             Model model) {
        if (!userService.isUsernameAvailable(signupForm.getUserName())) {
            model.addAttribute("signupError", "Username already exist");
            return "signup";
        }

        User user = new User();
        user.setFirstName(signupForm.getFirstName());
        user.setLastName(signupForm.getLastName());
        user.setUserName(signupForm.getUserName());
        user.setPassword(signupForm.getPassword());

        int rowsInserted = userService.createUser(user);

        if (rowsInserted < 1) {
            model.addAttribute("signupError", "Account registration error");
            return "redirect:/login";
        }

        model.addAttribute("successMessage", "You successfully signed up!");
        return "redirect:/login";
    }
}