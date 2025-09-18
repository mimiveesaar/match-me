package tech.kood.match_me.connections.features.pendingConnection.actions.createRequest.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import tech.kood.match_me.common.domain.api.UserIdDTO;
import tech.kood.match_me.connections.common.api.ConnectionIdDTO;

public record ConnectionRequestCreatedEvent(
        @NotNull @Valid @JsonProperty("connection_id") ConnectionIdDTO connectionIdDTO,
        @NotNull @Valid @JsonProperty("sender_id") UserIdDTO senderId,
        @NotNull @Valid @JsonProperty("target_id") UserIdDTO targetId
) {
}
