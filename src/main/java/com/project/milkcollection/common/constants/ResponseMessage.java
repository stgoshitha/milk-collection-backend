package com.project.milkcollection.common.constants;

//Standard response messages used across the application.
public final class ResponseMessage {

    private ResponseMessage() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated.");
    }

    //Common
    public static final String SUCCESS = "Success";
    public static final String FAILED = "Failed";

    public static final String CREATED_SUCCESSFULLY = "Created successfully";
    public static final String UPDATED_SUCCESSFULLY = "Updated successfully";
    public static final String DELETED_SUCCESSFULLY = "Deleted successfully";
    public static final String FETCH_SUCCESSFULLY = "Data retrieved successfully";

}