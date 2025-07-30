package tech.kood.match_me.user_management.internal.features.getUser.requests;

import jakarta.annotation.Nullable;

public record GetUserByIdRequest(String requestId, String userId, @Nullable String tracingId) {

    GetUserByIdRequest withUserId(String newUserId) {
        return new GetUserByIdRequest(requestId, newUserId, tracingId);
    }

    GetUserByIdRequest withTracingId(String newTracingId) {
        return new GetUserByIdRequest(requestId, userId, newTracingId);
    }
}
