package tech.kood.match_me.connections.features.pendingConnection.actions.declineRequest.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.jmolecules.architecture.cqrs.Command;
import tech.kood.match_me.connections.common.api.ConnectionIdDTO;

import java.util.UUID;

@Command
public record DeclineConnectionRequest(
        @NotNull @JsonProperty("request_id") UUID requestId,
        @NotNull @Valid @JsonProperty("connection_id") ConnectionIdDTO connectionId,
        @Nullable @JsonProperty("tracing_id") String tracingId
) {
    public DeclineConnectionRequest withRequestId(UUID requestId) {
        return new DeclineConnectionRequest(requestId, connectionId, tracingId);
    }
    public DeclineConnectionRequest withConnectionId(ConnectionIdDTO connectionId) {
        return new DeclineConnectionRequest(requestId, connectionId, tracingId);
    }
    public DeclineConnectionRequest withTracingId(String tracingId) {
        return new DeclineConnectionRequest(requestId, connectionId, tracingId);
    }
}
