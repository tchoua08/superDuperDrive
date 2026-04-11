package com.udacity.jwdnd.course1.cloudstorage.service;
import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.FileRecord;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class FileService {

    private final FileMapper fileMapper;

    public FileService(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    public List<FileRecord> getFilesByUserId(Integer userId) {
        return fileMapper.getFilesByUserId(userId);
    }

    public FileRecord getFileById(Integer fileId) {
        return fileMapper.getFileById(fileId);
    }

    public boolean isFilenameAvailable(String fileName, Integer userId) {
        return fileMapper.getFileByNameAndUserId(fileName, userId) == null;
    }

    public int uploadFile(MultipartFile multipartFile, Integer userId) throws IOException {
        FileRecord fileRecord = new FileRecord();
        fileRecord.setFileName(multipartFile.getOriginalFilename());
        fileRecord.setContentType(multipartFile.getContentType());
        fileRecord.setFileSize(String.valueOf(multipartFile.getSize()));
        fileRecord.setUserId(userId);
        fileRecord.setFileData(multipartFile.getBytes());

        return fileMapper.insert(fileRecord);
    }

    public int deleteFile(Integer fileId, Integer userId) {
        return fileMapper.delete(fileId, userId);
    }
}