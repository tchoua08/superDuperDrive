package com.udacity.jwdnd.course1.cloudstorage.controller;
import com.udacity.jwdnd.course1.cloudstorage.model.NoteForm;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.service.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
                           RedirectAttributes redirectAttributes) {
        User user = userService.getUser(authentication.getName());

        int result = noteService.saveNote(noteForm, user.getUserId());

        if (result > 0) {
            redirectAttributes.addFlashAttribute("successMessage", "Note saved successfully.");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Unable to save note.");
        }
        return "redirect:/result";
    }

    @GetMapping("/delete/{noteId}")
    public String deleteNote(@PathVariable Integer noteId,
                             Authentication authentication,
                             RedirectAttributes redirectAttributes) {
        User user = userService.getUser(authentication.getName());

        int result = noteService.deleteNote(noteId, user.getUserId());

        if (result > 0) {
            redirectAttributes.addFlashAttribute("successMessage", "Note deleted successfully.");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Unable to delete note.");
        }

        return "redirect:/result";
    }
}