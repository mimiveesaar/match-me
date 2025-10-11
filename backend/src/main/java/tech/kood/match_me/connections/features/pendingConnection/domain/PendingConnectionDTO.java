package tech.kood.match_me.connections.features.pendingConnection.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import tech.kood.match_me.common.domain.api.UserIdDTO;
import tech.kood.match_me.connections.common.api.ConnectionIdDTO;

import java.time.Instant;

public record PendingConnectionDTO(
        @NotNull @Valid @JsonProperty("connection_id") ConnectionIdDTO connectionIdDTO,
        @NotNull @Valid @JsonProperty("sender_id") UserIdDTO senderId,
        @NotNull @Valid @JsonProperty("target_id") UserIdDTO targetId,
        @NotNull @JsonProperty("created_at") Instant createdAt
) {

    public PendingConnectionDTO withConnectionIdDTO(ConnectionIdDTO connectionIdDTO) {
        return new PendingConnectionDTO(connectionIdDTO, senderId, targetId, createdAt);
    }

    public PendingConnectionDTO withSenderId(UserIdDTO senderId) {
        return new PendingConnectionDTO(connectionIdDTO, senderId, targetId, createdAt);
    }

    public PendingConnectionDTO withTargetId(UserIdDTO targetId) {
        return new PendingConnectionDTO(connectionIdDTO, senderId, targetId, createdAt);
    }

    public PendingConnectionDTO withCreatedAt(Instant createdAt) {
        return new PendingConnectionDTO(connectionIdDTO, senderId, targetId, createdAt);
    }
}