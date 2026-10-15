package com.project.milkcollection.common.file.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.project.milkcollection.common.file.dto.FileUploadResponse;
import com.project.milkcollection.exception.FileStorageException;
import com.project.milkcollection.common.file.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
@ConditionalOnProperty(
        name = "file.storage.provider",
        havingValue = "cloudinary"
)
public class CloudinaryFileStorageService
        implements FileStorageService {

    private final Cloudinary cloudinary;

    @Override
    public FileUploadResponse upload(
            MultipartFile file,
            String folder
    ) {

        try {

            Map<?, ?> result =
                    cloudinary.uploader()
                            .upload(
                                    file.getBytes(),
                                    ObjectUtils.asMap(
                                            "folder", folder,
                                            "resource_type", "image"
                                    )
                            );

            String url =
                    (String) result.get("secure_url");

            String publicId =
                    (String) result.get("public_id");

            return new FileUploadResponse(
                    url,
                    publicId
            );

        } catch (IOException exception) {
            throw new FileStorageException(
                    "Failed to upload image to Cloudinary",
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
            cloudinary.uploader()
                    .destroy(
                            publicId,
                            ObjectUtils.asMap(
                                    "resource_type",
                                    "image"
                            )
                    );

        } catch (Exception exception) {
            throw new FileStorageException(
                    "Failed to delete image from Cloudinary",
                    exception
            );
        }
    }
}