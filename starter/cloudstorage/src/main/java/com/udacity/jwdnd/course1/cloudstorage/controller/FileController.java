package com.udacity.jwdnd.course1.cloudstorage.controller;
import com.udacity.jwdnd.course1.cloudstorage.model.FileRecord;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.service.FileService;
import com.udacity.jwdnd.course1.cloudstorage.service.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/files")
public class FileController {

    private final FileService fileService;
    private final UserService userService;

    public FileController(FileService fileService, UserService userService) {
        this.fileService = fileService;
        this.userService = userService;
    }

    @PostMapping
    public String uploadFile(@RequestParam("fileUpload") MultipartFile multipartFile,
                             Authentication authentication,
                             RedirectAttributes redirectAttributes) {
        try {
            User user = userService.getUser(authentication.getName());

            if (multipartFile.isEmpty()) {
                redirectAttributes.addFlashAttribute("errorMessage", "Please select a file to upload.");
                return "redirect:/result";
            }

            if (!fileService.isFilenameAvailable(multipartFile.getOriginalFilename(), user.getUserId())) {
                redirectAttributes.addFlashAttribute("errorMessage", "A file with the same name already exists.");
                return "redirect:/result";
            }

            int result = fileService.uploadFile(multipartFile, user.getUserId());

            if (result > 0) {
                redirectAttributes.addFlashAttribute("successMessage", "File uploaded successfully.");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Unable to upload file.");
            }

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Unexpected error while uploading file.");
        }

        return "redirect:/result";
    }

    @GetMapping("/download/{fileId}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable Integer fileId, Authentication authentication) {
        User user = userService.getUser(authentication.getName());
        FileRecord fileRecord = fileService.getFileById(fileId);

        if (fileRecord == null || !fileRecord.getUserId().equals(user.getUserId())) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(fileRecord.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileRecord.getFileName() + "\"")
                .body(fileRecord.getFileData());
    }

    @GetMapping("/delete/{fileId}")
    public String deleteFile(@PathVariable Integer fileId,
                             Authentication authentication,
                             RedirectAttributes redirectAttributes) {
        User user = userService.getUser(authentication.getName());

        int result = fileService.deleteFile(fileId, user.getUserId());

        if (result > 0) {
            redirectAttributes.addFlashAttribute("successMessage", "File deleted successfully.");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Unable to delete file.");
        }

        return "redirect:/result";
    }
}