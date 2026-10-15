package com.project.milkcollection.common.file.service;

import com.project.milkcollection.common.file.dto.FileUploadResponse;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {

    FileUploadResponse upload(
            MultipartFile file,
            String folder
    );

    void delete(String publicId);
}