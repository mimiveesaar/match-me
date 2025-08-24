package tech.kood.match_me.connections.features.rejectedConnection.domain.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import tech.kood.match_me.common.domain.api.UserIdDTO;
import tech.kood.match_me.connections.common.api.ConnectionIdDTO;

import java.time.Instant;

public record RejectedConnectionDTO(
        @NotNull @Valid @JsonProperty("connection_id") ConnectionIdDTO connectionIdDTO,
        @NotNull @Valid @JsonProperty("rejected_by_user") UserIdDTO rejectedByUser,
        @NotNull @Valid @JsonProperty("rejectedUser") UserIdDTO rejectedUser,
        @NotNull @Valid @JsonProperty("reason") RejectedConnectionReasonDTO reason,
        @NotNull@JsonProperty("created_at")Instant createdAt
) {

    public RejectedConnectionDTO withConnectionIdDTO(ConnectionIdDTO connectionIdDTO) {
        return new RejectedConnectionDTO(connectionIdDTO, rejectedByUser, rejectedUser, reason, createdAt);
    }

    public RejectedConnectionDTO withRejectedBy(UserIdDTO rejectedBy) {
        return new RejectedConnectionDTO(connectionIdDTO, rejectedBy, rejectedUser, reason, createdAt);
    }

    public RejectedConnectionDTO withRejected(UserIdDTO rejected) {
        return new RejectedConnectionDTO(connectionIdDTO, rejectedByUser, rejected, reason, createdAt);
    }

    public RejectedConnectionDTO withReason(RejectedConnectionReasonDTO reason) {
        return new RejectedConnectionDTO(connectionIdDTO, rejectedByUser, rejectedUser, reason, createdAt);
    }

    public RejectedConnectionDTO withCreatedAt(Instant createdAt) {
        return new RejectedConnectionDTO(connectionIdDTO, rejectedByUser, rejectedUser, reason, createdAt);
    }
}