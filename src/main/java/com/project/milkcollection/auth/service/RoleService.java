package com.project.milkcollection.auth.service;

import com.project.milkcollection.auth.dto.request.CreateRoleRequest;
import com.project.milkcollection.auth.dto.response.RoleResponse;

public interface RoleService {

    RoleResponse createRole(CreateRoleRequest createRoleRequest);
}
