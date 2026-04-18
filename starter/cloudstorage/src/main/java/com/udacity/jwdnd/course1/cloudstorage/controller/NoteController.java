package com.udacity.jwdnd.course1.cloudstorage.controller;
import com.udacity.jwdnd.course1.cloudstorage.model.NoteForm;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.service.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/notes")
public class NoteController {

    private final NoteService noteService;
    private final UserService userService;

    public NoteController(NoteService noteService, UserService userService) {
        this.noteService = noteService;
        this.userService = userService;
    }

    @PostMapping
    public String saveNote(@ModelAttribute NoteForm noteForm,
                           Authentication authentication,
                           Model model) {
        User user = userService.getUser(authentication.getName());

        int result = noteService.saveNote(noteForm, user.getUserId());

        if (result > 0) {
            model.addAttribute("successMessage", "Note Successfully save");
        } else {
            model.addAttribute("errorMessage", "Registration error");
        }

        return "result";
    }

    @GetMapping("/delete/{noteId}")
    public String deleteNote(@PathVariable Integer noteId,
                             Authentication authentication,
                             Model model) {
        User user = userService.getUser(authentication.getName());

        int result = noteService.deleteNote(noteId, user.getUserId());

        if (result > 0) {
            model.addAttribute("successMessage", "Delete note Success");
        } else {
            model.addAttribute("errorMessage", "Error deleted note");
        }

        return "result";
    }
}