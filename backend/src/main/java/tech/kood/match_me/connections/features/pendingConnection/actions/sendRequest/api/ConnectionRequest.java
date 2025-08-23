package tech.kood.match_me.connections.features.pendingConnection.actions.sendRequest.api;

import java.io.Serializable;
import java.util.UUID;

import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import tech.kood.match_me.user_management.common.domain.api.UserIdDTO;

public record ConnectionRequest(@NotNull UUID requestId, @NotNull @Valid UserIdDTO targetUserId,
                                @NotNull @Valid UserIdDTO senderId,
                                @Nullable String tracingId) implements Serializable {

    public ConnectionRequest withRequestId(UUID requestId) {
        return new ConnectionRequest(requestId, targetUserId, senderId, tracingId);
    }

    public ConnectionRequest withTargetUserId(UserIdDTO targetUserId) {
        return new ConnectionRequest(requestId, targetUserId, senderId, tracingId);
    }

    public ConnectionRequest withSenderId(UserIdDTO senderId) {
        return new ConnectionRequest(requestId, targetUserId, senderId, tracingId);
    }

    public ConnectionRequest withTracingId(String tracingId) {
        return new ConnectionRequest(requestId, targetUserId, senderId, tracingId);
    }
}