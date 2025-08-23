package tech.kood.match_me.connections.features.pendingConnection.actions.declineRequest.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import tech.kood.match_me.connections.common.api.ConnectionIdDTO;

public record ConnectionDeclinedEvent(@NotNull @Valid @JsonProperty("connection_id") ConnectionIdDTO connectionId) {
}
