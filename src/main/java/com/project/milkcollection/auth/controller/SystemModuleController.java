package com.project.milkcollection.auth.controller;

import com.project.milkcollection.auth.dto.request.systemmodule.CreateSystemModuleRequest;
import com.project.milkcollection.auth.dto.request.systemmodule.UpdateSystemModuleOrderRequest;
import com.project.milkcollection.auth.dto.request.systemmodule.UpdateSystemModuleRequest;
import com.project.milkcollection.auth.dto.request.systemmodule.UpdateSystemModuleStatusRequest;
import com.project.milkcollection.auth.dto.response.SystemModuleResponse;
import com.project.milkcollection.auth.service.SystemModuleService;
import com.project.milkcollection.common.constants.ResponseMessage;
import com.project.milkcollection.common.constants.SecurityConstants;
import com.project.milkcollection.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(SecurityConstants.SYSTEM_MODULE_BASE_URL)
@RequiredArgsConstructor
public class SystemModuleController {

    private final SystemModuleService systemModuleService;

    // Create a new system module
    @PostMapping
    public ResponseEntity<ApiResponse<SystemModuleResponse>> createSystemModule(
            @Valid @RequestBody CreateSystemModuleRequest request) {

        SystemModuleResponse systemModule = systemModuleService.createSystemModule(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        ApiResponse.success(
                                "System module " + ResponseMessage.CREATED_SUCCESSFULLY,
                                systemModule
                        )
                );
    }

    // Get a system module by ID
    @GetMapping("/{systemModuleId}")
    public ResponseEntity<ApiResponse<SystemModuleResponse>> getSystemModuleById(
            @PathVariable UUID systemModuleId) {

        SystemModuleResponse systemModule =
                systemModuleService.getSystemModuleById(systemModuleId);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "System module " + ResponseMessage.FETCH_SUCCESSFULLY,
                        systemModule)
        );
    }

    // Get all system modules
    @GetMapping
    public ResponseEntity<ApiResponse<List<SystemModuleResponse>>> getAllSystemModules() {

        List<SystemModuleResponse> systemModules =
                systemModuleService.getAllSystemModules();

        return ResponseEntity.ok(
                ApiResponse.success(
                        "System modules " + ResponseMessage.FETCH_SUCCESSFULLY,
                        systemModules)
        );
    }

    // Update system module details
    @PutMapping("/{systemModuleId}")
    public ResponseEntity<ApiResponse<SystemModuleResponse>> updateSystemModule(
            @PathVariable UUID systemModuleId,
            @Valid @RequestBody UpdateSystemModuleRequest updateSystemModuleRequest) {

        SystemModuleResponse systemModule =
                systemModuleService.updateSystemModule(systemModuleId, updateSystemModuleRequest);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "System module " + ResponseMessage.UPDATED_SUCCESSFULLY,
                        systemModule
                )
        );
    }

    // Activate or deactivate a system module
    @PatchMapping("/{systemModuleId}/status")
    public ResponseEntity<ApiResponse<SystemModuleResponse>> updateModuleStatus(
            @PathVariable UUID systemModuleId,
            @Valid @RequestBody UpdateSystemModuleStatusRequest request) {

        SystemModuleResponse systemModule =
                systemModuleService.updateModuleStatus(
                        systemModuleId,
                        request
                );

        return ResponseEntity.ok(
                ApiResponse.success(
                        "System module status " + ResponseMessage.UPDATED_SUCCESSFULLY,
                        systemModule
                )
        );
    }

    // Update system module order
    @PatchMapping("/order")
    public ResponseEntity<ApiResponse<Void>> updateModuleOrder(
            @Valid @RequestBody UpdateSystemModuleOrderRequest updateSystemModuleOrderRequest) {

        systemModuleService.updateSystemModuleOrder(updateSystemModuleOrderRequest);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "System module order "
                                + ResponseMessage.UPDATED_SUCCESSFULLY
                )
        );
    }
}