package tech.kood.match_me.connections.features.rejectedConnection.actions.deleteRejectedConnection.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.jmolecules.architecture.cqrs.Command;
import tech.kood.match_me.connections.common.api.ConnectionId;

import java.util.UUID;

@Command
public record DeleteRejectedConnectionRequest(@NotNull @JsonProperty("request_id") UUID requestId,
                                              @NotNull @Valid @JsonProperty("connection_id") ConnectionId connectionId,
                                              @Nullable @JsonProperty("tracing_id") String tracingId) {

    public DeleteRejectedConnectionRequest withRequestId(UUID requestId) {
        return new DeleteRejectedConnectionRequest(requestId, connectionId, tracingId);
    }

    public DeleteRejectedConnectionRequest withConnectionId(ConnectionId connectionId) {
        return new DeleteRejectedConnectionRequest(requestId, connectionId, tracingId);
    }

    public DeleteRejectedConnectionRequest withTracingId(String tracingId) {
        return new DeleteRejectedConnectionRequest(requestId, connectionId, tracingId);
    }
}
