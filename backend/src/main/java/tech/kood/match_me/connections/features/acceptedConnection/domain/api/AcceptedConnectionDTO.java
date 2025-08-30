package tech.kood.match_me.connections.features.acceptedConnection.domain.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import tech.kood.match_me.connections.common.api.ConnectionIdDTO;
import tech.kood.match_me.common.domain.api.UserIdDTO;

import java.time.Instant;

public record AcceptedConnectionDTO(
        @NotNull @Valid @JsonProperty("connection_id") ConnectionIdDTO connectionIdDTO,
        @NotNull @Valid @JsonProperty("accepted_by_user") UserIdDTO acceptedByUser,
        @NotNull @Valid @JsonProperty("accepted_user") UserIdDTO acceptedUser,
        @NotNull @JsonProperty("created_at") Instant createdAt) {

    public AcceptedConnectionDTO withConnectionIdDTO(ConnectionIdDTO connectionIdDTO) {
        return new AcceptedConnectionDTO(connectionIdDTO, acceptedByUser, acceptedUser, createdAt);
    }

    public AcceptedConnectionDTO withAcceptedByUser(UserIdDTO acceptedByUser) {
        return new AcceptedConnectionDTO(connectionIdDTO, acceptedByUser, acceptedUser, createdAt);
    }

    public AcceptedConnectionDTO withAcceptedUser(UserIdDTO acceptedUser) {
        return new AcceptedConnectionDTO(connectionIdDTO, acceptedByUser, acceptedUser, createdAt);
    }

    public AcceptedConnectionDTO withCreatedAt(Instant createdAt) {
        return new AcceptedConnectionDTO(connectionIdDTO, acceptedByUser, acceptedUser, createdAt);
    }
}
