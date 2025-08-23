package tech.kood.match_me.connections.features.pendingConnection.actions.createRequest.api;

import java.io.Serializable;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.jmolecules.architecture.cqrs.Command;
import tech.kood.match_me.common.domain.api.UserIdDTO;

@Command
public record ConnectionRequest(@NotNull @JsonProperty("request_id") UUID requestId,
                                @NotNull @Valid @JsonProperty("target_id") UserIdDTO targetId,
                                @NotNull @Valid @JsonProperty("sender_id") UserIdDTO senderId,
                                @Nullable @JsonProperty("tracing_id") String tracingId) implements Serializable {

    public ConnectionRequest withRequestId(UUID requestId) {
        return new ConnectionRequest(requestId, targetId, senderId, tracingId);
    }

    public ConnectionRequest withTargetUserId(UserIdDTO targetUserId) {
        return new ConnectionRequest(requestId, targetUserId, senderId, tracingId);
    }

    public ConnectionRequest withSenderId(UserIdDTO senderId) {
        return new ConnectionRequest(requestId, targetId, senderId, tracingId);
    }

    public ConnectionRequest withTracingId(String tracingId) {
        return new ConnectionRequest(requestId, targetId, senderId, tracingId);
    }
}