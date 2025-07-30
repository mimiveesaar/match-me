package tech.kood.match_me.user_management.internal.features.login;

import jakarta.annotation.Nullable;

public record LoginRequest(String requestId, String email, String password,
        @Nullable String tracingId) {
}
