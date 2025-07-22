package tech.kood.match_me.user_management.internal.features.getUser.requests;

import java.util.Optional;
import java.util.UUID;

public record GetUserByEmailRequest(
    UUID requestId,
    String email,
    Optional<String> tracingId
) {

    GetUserByEmailRequest withEmail(String newEmail) {
        return new GetUserByEmailRequest(requestId, newEmail, tracingId);
    }

    GetUserByEmailRequest withTracingId(String newTracingId) {
        return new GetUserByEmailRequest(requestId, email, Optional.ofNullable(newTracingId));
    }
}