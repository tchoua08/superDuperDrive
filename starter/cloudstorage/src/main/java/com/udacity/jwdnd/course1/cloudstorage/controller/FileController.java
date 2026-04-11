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
                             Model model) {
        try {
            User user = userService.getUser(authentication.getName());

            if (multipartFile.isEmpty()) {
                model.addAttribute("errorMessage", "Please add a file");
                return "result";
            }

            if (!fileService.isFilenameAvailable(multipartFile.getOriginalFilename(), user.getUserId())) {
                model.addAttribute("errorMessage", "Error a file with the same name already exist");
                return "result";
            }

            int result = fileService.uploadFile(multipartFile, user.getUserId());

            if (result > 0) {
                model.addAttribute("successMessage", "File upload success");
            } else {
                model.addAttribute("errorMessage", "upload file error");
            }

        } catch (Exception e) {
            model.addAttribute("errorMessage", "Technical error uploading file");
        }

        return "result";
    }

    @GetMapping("/download/{fileid}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable Integer fileid, Authentication authentication) {
        User user = userService.getUser(authentication.getName());
        FileRecord fileRecord = fileService.getFileById(fileid);

        if (fileRecord == null || !fileRecord.getUserId().equals(user.getUserId())) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(fileRecord.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileRecord.getFileName() + "\"")
                .body(fileRecord.getFileData());
    }

    @GetMapping("/delete/{fileid}")
    public String deleteFile(@PathVariable Integer fileid,
                             Authentication authentication,
                             Model model) {
        User user = userService.getUser(authentication.getName());

        int result = fileService.deleteFile(fileid, user.getUserId());

        if (result > 0) {
            model.addAttribute("successMessage", "File deleted successfully");
        } else {
            model.addAttribute("errorMessage", "Error Deleted file");
        }

        return "result";
    }
}