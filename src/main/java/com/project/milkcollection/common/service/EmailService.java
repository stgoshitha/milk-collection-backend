package com.project.milkcollection.common.service;

public interface EmailService {

    void sendPasswordResetEmail(
            String email,
            String token
    );
}