package tech.kood.match_me.user_management.internal.features.getUser.requests;

import java.util.Optional;
import java.util.UUID;

public record GetUserByIdRequest(
    UUID requestId,
    UUID userId, 
    Optional<String> tracingId) {

    GetUserByIdRequest withUserId(UUID newUserId) {
        return new GetUserByIdRequest(requestId, newUserId, tracingId);
    }
    GetUserByIdRequest withTracingId(String newTracingId) {
        return new GetUserByIdRequest(requestId, userId, Optional.ofNullable(newTracingId));
    }
}