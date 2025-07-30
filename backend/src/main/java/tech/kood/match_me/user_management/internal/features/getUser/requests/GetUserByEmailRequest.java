package tech.kood.match_me.user_management.internal.features.getUser.requests;

import jakarta.annotation.Nullable;

public record GetUserByEmailRequest(String requestId, String email, @Nullable String tracingId) {

    GetUserByEmailRequest withEmail(String newEmail) {
        return new GetUserByEmailRequest(requestId, newEmail, tracingId);
    }

    GetUserByEmailRequest withTracingId(String newTracingId) {
        return new GetUserByEmailRequest(requestId, email, newTracingId);
    }
}
