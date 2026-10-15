package com.project.milkcollection.common.file.constants;

/**
 * Constants related to file storage.
 */
public final class FileStorageConstants {

    private FileStorageConstants() {
        throw new UnsupportedOperationException(
                "Utility class cannot be instantiated."
        );
    }

    // Default folders
    public static final String PROFILE_FOLDER = "profiles";
    public static final String FARMER_FOLDER = "farmers";
    public static final String DOCUMENT_FOLDER = "documents";

    // File validation
    public static final long DEFAULT_MAX_FILE_SIZE = 5L * 1024 * 1024;

    // Content types
    public static final String IMAGE_JPEG = "image/jpeg";
    public static final String IMAGE_PNG = "image/png";
    public static final String IMAGE_WEBP = "image/webp";

    // File extensions
    public static final String JPEG_EXTENSION = ".jpg";
    public static final String PNG_EXTENSION = ".png";
    public static final String WEBP_EXTENSION = ".webp";
}