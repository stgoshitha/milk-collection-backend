package com.project.milkcollection.common.dto;

import lombok.Builder;

//Represents a single API validation or business error.
@Builder
public record ApiError(String field, String message) {

}