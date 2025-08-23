package tech.kood.match_me.connections.features.rejectedConnection.actions.deleteRejectedConnection.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.jmolecules.architecture.cqrs.Command;
import tech.kood.match_me.connections.common.api.ConnectionIdDTO;

import java.util.UUID;

@Command
public record DeleteRejectedConnection(@NotNull @JsonProperty("request_id") UUID requestId,
        @NotNull @Valid @JsonProperty("connection_id") ConnectionIdDTO connectionId,
        @Nullable @JsonProperty("tracing_id") String tracingId) {
    public DeleteRejectedConnection withRequestId(UUID requestId) {
        return new DeleteRejectedConnection(requestId, connectionId, tracingId);
    }

    public DeleteRejectedConnection withConnectionId(ConnectionIdDTO connectionId) {
        return new DeleteRejectedConnection(requestId, connectionId, tracingId);
    }

    public DeleteRejectedConnection withTracingId(String tracingId) {
        return new DeleteRejectedConnection(requestId, connectionId, tracingId);
    }
}
