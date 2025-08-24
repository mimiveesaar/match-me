package tech.kood.match_me.connections.features.rejectedConnection.domain.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import tech.kood.match_me.common.domain.api.UserIdDTO;
import tech.kood.match_me.connections.common.api.ConnectionId;

import java.time.Instant;

public record RejectedConnectionDTO(
        @NotNull @Valid @JsonProperty("connection_id") ConnectionId connectionId,
        @NotNull @Valid @JsonProperty("rejected_by_user") UserIdDTO rejectedByUser,
        @NotNull @Valid @JsonProperty("rejectedUser") UserIdDTO rejectedUser,
        @NotNull @Valid @JsonProperty("reason") RejectedConnectionReasonDTO reason,
        @NotNull@JsonProperty("created_at")Instant createdAt
) {

    public RejectedConnectionDTO withConnectionIdDTO(ConnectionId connectionIdDTO) {
        return new RejectedConnectionDTO(connectionIdDTO, rejectedByUser, rejectedUser, reason, createdAt);
    }

    public RejectedConnectionDTO withRejectedBy(UserIdDTO rejectedBy) {
        return new RejectedConnectionDTO(connectionId, rejectedBy, rejectedUser, reason, createdAt);
    }

    public RejectedConnectionDTO withRejected(UserIdDTO rejected) {
        return new RejectedConnectionDTO(connectionId, rejectedByUser, rejected, reason, createdAt);
    }

    public RejectedConnectionDTO withReason(RejectedConnectionReasonDTO reason) {
        return new RejectedConnectionDTO(connectionId, rejectedByUser, rejectedUser, reason, createdAt);
    }

    public RejectedConnectionDTO withCreatedAt(Instant createdAt) {
        return new RejectedConnectionDTO(connectionId, rejectedByUser, rejectedUser, reason, createdAt);
    }
}