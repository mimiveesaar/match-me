package tech.kood.match_me.connections.features.rejectedConnection.actions.createRejectedConnection.api;

import java.io.Serializable;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.jmolecules.architecture.cqrs.Command;
import tech.kood.match_me.common.domain.api.UserIdDTO;
import tech.kood.match_me.connections.features.rejectedConnection.actions.createRejectedConnection.api.validation.ValidCreateRejectedConnection;
import tech.kood.match_me.connections.features.rejectedConnection.domain.api.RejectedConnectionReasonDTO;

@Command
@ValidCreateRejectedConnection
public record CreateRejectedConnection(@NotNull @JsonProperty("request_id") UUID requestId,
        @NotNull @Valid @JsonProperty("rejected_by_user") UserIdDTO rejectedByUser,
        @NotNull @Valid @JsonProperty("rejected_user") UserIdDTO rejectedUser,
        @NotNull @JsonProperty("reason") RejectedConnectionReasonDTO reason,
        @Nullable @JsonProperty("tracing_id") String tracingId) implements Serializable {

    public CreateRejectedConnection withRequestId(UUID requestId) {
        return new CreateRejectedConnection(requestId, rejectedByUser, rejectedUser, reason,
                tracingId);
    }

    public CreateRejectedConnection withRejectedByUser(UserIdDTO rejectedByUser) {
        return new CreateRejectedConnection(requestId, rejectedByUser, rejectedUser, reason,
                tracingId);
    }

    public CreateRejectedConnection withRejectedUser(UserIdDTO rejectedUser) {
        return new CreateRejectedConnection(requestId, rejectedByUser, rejectedUser, reason,
                tracingId);
    }

    public CreateRejectedConnection withReason(RejectedConnectionReasonDTO reason) {
        return new CreateRejectedConnection(requestId, rejectedByUser, rejectedUser, reason,
                tracingId);
    }

    public CreateRejectedConnection withTracingId(String tracingId) {
        return new CreateRejectedConnection(requestId, rejectedByUser, rejectedUser, reason,
                tracingId);
    }
}
