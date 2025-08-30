package tech.kood.match_me.connections.features.pendingConnection.actions.createRequest.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import tech.kood.match_me.connections.common.api.ConnectionIdDTO;

public record ConnectionRequestCreatedEvent(@NotNull @JsonProperty("connection_id") ConnectionIdDTO connectionIdDTO) {
}
