package tech.kood.match_me.user_management.internal.features.getUser.requests;

import java.util.Optional;
import java.util.UUID;

import lombok.Builder;

@Builder
public record GetUserByEmailRequest(
    UUID requestId,
    String email,
    Optional<String> tracingId
    ) {
    public GetUserByEmailRequest {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email cannot be null or blank");
        }
        if (tracingId == null) {
            throw new IllegalArgumentException("Tracing ID cannot be null");
        }
    }
}