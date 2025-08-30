package tech.kood.match_me.connections.features.rejectedConnection.actions.deleteRejectedConnection.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.jmolecules.architecture.cqrs.Command;
import tech.kood.match_me.connections.common.api.ConnectionIdDTO;

import java.util.UUID;

@Command
public record DeleteRejectedConnectionRequest(@NotNull @JsonProperty("request_id") UUID requestId,
                                              @NotNull @Valid @JsonProperty("connection_id") ConnectionIdDTO connectionIdDTO,
                                              @Nullable @JsonProperty("tracing_id") String tracingId) {

    public DeleteRejectedConnectionRequest withRequestId(UUID requestId) {
        return new DeleteRejectedConnectionRequest(requestId, connectionIdDTO, tracingId);
    }

    public DeleteRejectedConnectionRequest withConnectionId(ConnectionIdDTO connectionIdDTO) {
        return new DeleteRejectedConnectionRequest(requestId, connectionIdDTO, tracingId);
    }

    public DeleteRejectedConnectionRequest withTracingId(String tracingId) {
        return new DeleteRejectedConnectionRequest(requestId, connectionIdDTO, tracingId);
    }
}
