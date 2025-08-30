package tech.kood.match_me.connections.features.acceptedConnection.actions.rejectAcceptedConnection.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.jmolecules.architecture.cqrs.Command;
import tech.kood.match_me.common.domain.api.UserIdDTO;
import tech.kood.match_me.connections.common.api.ConnectionIdDTO;

import java.util.UUID;

@Command
public record RejectAcceptedConnectionRequest(@NotNull @JsonProperty("request_id") UUID requestId,
                                              @NotNull @Valid @JsonProperty("connection_id") ConnectionIdDTO connectionId,
                                              @NotNull @Valid @JsonProperty("user_id") UserIdDTO rejectedBy,
                                              @Nullable @JsonProperty("tracing_id") String tracingId) {

    public RejectAcceptedConnectionRequest withRequestId(UUID requestId) {
        return new RejectAcceptedConnectionRequest(requestId, connectionId, rejectedBy, tracingId);
    }

    public RejectAcceptedConnectionRequest withConnectionId(ConnectionIdDTO connectionIdDTO) {
        return new RejectAcceptedConnectionRequest(requestId, connectionIdDTO, rejectedBy, tracingId);
    }

    public RejectAcceptedConnectionRequest withRejectedBy(UserIdDTO rejectedBy) {
        return new RejectAcceptedConnectionRequest(requestId, connectionId, rejectedBy, tracingId);
    }

    public RejectAcceptedConnectionRequest withTracingId(String tracingId) {
        return new RejectAcceptedConnectionRequest(requestId, connectionId, rejectedBy, tracingId);
    }
}
