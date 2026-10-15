package com.project.milkcollection.common.file.validator;

import com.project.milkcollection.common.file.config.FileStorageConfig;
import com.project.milkcollection.common.file.constants.FileStorageConstants;
import com.project.milkcollection.exception.FileStorageException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class ImageFileValidator {

    private final FileStorageConfig fileStorageConfig;

    private static final Set<String> ALLOWED_CONTENT_TYPES =
            Set.of(
                    FileStorageConstants.IMAGE_JPEG,
                    FileStorageConstants.IMAGE_PNG,
                    FileStorageConstants.IMAGE_WEBP
            );

    public void validate(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new FileStorageException(
                    "Image file is required"
            );
        }

        if (file.getSize() > fileStorageConfig.maxFileSize()) {
            throw new FileStorageException(
                    "Image size cannot exceed 5 MB"
            );
        }

        String contentType = file.getContentType();

        if (contentType == null
                || !ALLOWED_CONTENT_TYPES.contains(
                contentType.toLowerCase()
        )) {

            throw new FileStorageException(
                    "Only JPEG, PNG and WEBP images are allowed"
            );
        }
    }
}