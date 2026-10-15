package com.project.milkcollection.common.file.service.impl;

import com.project.milkcollection.common.file.config.FileStorageConfig;
import com.project.milkcollection.common.file.dto.FileUploadResponse;
import com.project.milkcollection.common.file.validator.ImageFileValidator;
import com.project.milkcollection.exception.FileStorageException;
import com.project.milkcollection.common.file.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@ConditionalOnProperty(
        name = "file.storage.provider",
        havingValue = "local"
)
public class LocalFileStorageService
        implements FileStorageService {

    private final FileStorageConfig fileStorageConfig;
    private final ImageFileValidator imageFileValidator;

    @Override
    public FileUploadResponse upload(
            MultipartFile file,
            String folder
    ) {

        imageFileValidator.validate(file);

        try {
            Path uploadDirectory =
                    Paths.get(
                            fileStorageConfig.localDirectory(),
                            folder
                    );

            Files.createDirectories(uploadDirectory);

            String extension =
                    StringUtils.getFilenameExtension(
                            file.getOriginalFilename()
                    );

            String filename =
                    UUID.randomUUID()
                            + (extension != null
                            ? "." + extension
                            : "");

            Path filePath =
                    uploadDirectory.resolve(filename);

            Files.copy(
                    file.getInputStream(),
                    filePath
            );

            return new FileUploadResponse(
                    filePath.toString(),
                    filePath.toString()
            );

        } catch (IOException exception) {
            throw new FileStorageException(
                    "Failed to store file",
                    exception
            );
        }
    }

    @Override
    public void delete(String publicId) {

        if (publicId == null || publicId.isBlank()) {
            return;
        }

        try {

            Files.deleteIfExists(
                    Paths.get(publicId)
            );

        } catch (IOException exception) {
            throw new FileStorageException(
                    "Failed to delete file",
                    exception
            );
        }
    }
}