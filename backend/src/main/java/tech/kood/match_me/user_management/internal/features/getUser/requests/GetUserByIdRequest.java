package tech.kood.match_me.user_management.internal.features.getUser.requests;

import java.util.Optional;
import java.util.UUID;

import lombok.Builder;

@Builder
public record GetUserByIdRequest(
    UUID requestId,
    UUID userId, 
    Optional<String> tracingId) {
    public GetUserByIdRequest {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        if (tracingId == null) {
            throw new IllegalArgumentException("Tracing ID cannot be null");
        }
        if (requestId == null) {
            throw new IllegalArgumentException("Request ID cannot be null");
        }
    }
}