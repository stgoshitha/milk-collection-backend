package com.project.milkcollection.common.response;

import com.project.milkcollection.common.dto.ApiError;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Standard API error response.
 *
 * @param success   Indicates whether the request was successful.
 * @param message   General error message.
 * @param errors    Detailed validation or business errors.
 * @param timestamp Time when the error occurred.
 */
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiErrorResponse(
        boolean success,
        String message,
        List<ApiError> errors,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime timestamp
) {

    //Creates an error response with detailed errors
    public static ApiErrorResponse of(
            String message,
            List<ApiError> errors
    ) {
        return ApiErrorResponse.builder()
                .success(false)
                .message(message)
                .errors(errors)
                .timestamp(LocalDateTime.now())
                .build();
    }

    //Creates an error response without validation details
    public static ApiErrorResponse of(
            String message
    ) {
        return ApiErrorResponse.builder()
                .success(false)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }
}