package com.project.milkcollection.auth.service;

public interface PasswordResetTokenHashService {

    String hash(String token);

}
