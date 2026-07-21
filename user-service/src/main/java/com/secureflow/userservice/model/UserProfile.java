package com.secureflow.userservice.model;

import java.util.List;

public record UserProfile(
        String username,
        String email,
        List<String> roles,
        String tokenSubject
) {}
