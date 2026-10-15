package com.project.milkcollection.auth.dto.request.systemmodule;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record UpdateSystemModuleOrderRequest(

        @NotEmpty(message = "Module order list cannot be empty")
        List<UpdateSystemModuleOrderItem> systemModules

) {
}
