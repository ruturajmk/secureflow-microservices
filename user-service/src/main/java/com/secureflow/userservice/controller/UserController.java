package com.secureflow.userservice.controller;

import com.secureflow.userservice.model.UserProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private static final Logger log =
            LoggerFactory.getLogger(UserController.class);

    // ── GET /api/users/me — returns current user info from JWT ────
    @GetMapping("/me")
    public ResponseEntity<UserProfile> getCurrentUser(
            @AuthenticationPrincipal Jwt jwt) {

        String username = jwt.getSubject();
        String email    = jwt.getClaimAsString("email");
        List<String> roles = jwt.getClaimAsStringList("roles");

        UserProfile profile = new UserProfile(
                username,
                email != null ? email : "not provided",
                roles  != null ? roles : List.of(),
                jwt.getSubject()
        );

        return ResponseEntity.ok(profile);
    }

    // ── GET /api/users/admin — only ROLE_ADMIN can access ─────────
    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Map<String, Object>> adminOnly(
            @AuthenticationPrincipal Jwt jwt) {

        return ResponseEntity.ok(Map.of(
                "message", "Welcome admin: " + jwt.getSubject(),
                "allClaims", jwt.getClaims()
        ));
    }

    // ── GET /api/users/public — no auth needed ────────────────────
    @GetMapping("/public")
    public ResponseEntity<Map<String, String>> publicEndpoint() {
        return ResponseEntity.ok(Map.of(
                "message", "This endpoint is public",
                "service", "user-service"
        ));
    }
}
