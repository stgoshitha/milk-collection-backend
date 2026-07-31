package com.project.milkcollection.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

/**
 * Standard API response wrapper for all REST endpoints.
 *
 * @param success Indicates whether the request was successful.
 * @param message Human-readable response message.
 * @param data    Response payload.
 * @param <T>     Type of response data.
 */
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse<T>(boolean success, String message, T data) {

    //Creates a successful response with data.
    public static <T> ApiResponse<T> success(String message, T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .build();
    }

    //Creates a successful response without data.
    public static <T> ApiResponse<T> success(String message) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .build();
    }

    //Creates an error response.
    public static <T> ApiResponse<T> error(String message) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .build();
    }

    //Creates an error response with additional details.
    public static <T> ApiResponse<T> error(String message, T data) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .data(data)
                .build();
    }
}