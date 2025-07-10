package tech.kood.match_me.user_management.internal.features.getUser.requests;

import java.util.Optional;
import java.util.UUID;

import lombok.Builder;

@Builder
public record GetUserByIdRequest(UUID id, Optional<String> tracingId) {
    public GetUserByIdRequest {
        if (id == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        if (tracingId == null) {
            throw new IllegalArgumentException("Tracing ID cannot be null");
        }
    }
}