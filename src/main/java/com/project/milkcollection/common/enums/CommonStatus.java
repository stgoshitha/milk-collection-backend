package com.project.milkcollection.common.enums;

import lombok.Getter;

@Getter
public enum CommonStatus {

    ACTIVE("Active"),
    INACTIVE("Inactive");

    private final String displayCommonStatus;

    CommonStatus(String displayCommonStatus ){
        this.displayCommonStatus = displayCommonStatus;
    }

}
