package com.project.milkcollection.auth.entity.enums;

import lombok.Getter;

@Getter
public enum UserStatus {

    ACTIVE("Active"),
    INACTIVE("Inactive"),
    SUSPENDED("Suspended");

    private final String displayUserStatus;

    UserStatus(String displayUserStatus){
        this.displayUserStatus = displayUserStatus;
    }

}
