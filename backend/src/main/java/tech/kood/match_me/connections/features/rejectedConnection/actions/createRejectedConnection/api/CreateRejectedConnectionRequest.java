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
public record CreateRejectedConnectionRequest(@NotNull @JsonProperty("request_id") UUID requestId,
                                              @NotNull @Valid @JsonProperty("rejected_by_user") UserIdDTO rejectedByUser,
                                              @NotNull @Valid @JsonProperty("rejected_user") UserIdDTO rejectedUser,
                                              @NotNull @JsonProperty("reason") RejectedConnectionReasonDTO reason,
                                              @Nullable @JsonProperty("tracing_id") String tracingId) implements Serializable {


    public CreateRejectedConnectionRequest(UserIdDTO rejectedByUser, UserIdDTO rejectedUser, RejectedConnectionReasonDTO reason, @Nullable String tracingId) {
        this(UUID.randomUUID(), rejectedByUser, rejectedUser, reason, tracingId);
    }

    public CreateRejectedConnectionRequest withRequestId(UUID requestId) {
        return new CreateRejectedConnectionRequest(requestId, rejectedByUser, rejectedUser, reason,
                tracingId);
    }

    public CreateRejectedConnectionRequest withRejectedByUser(UserIdDTO rejectedByUser) {
        return new CreateRejectedConnectionRequest(requestId, rejectedByUser, rejectedUser, reason,
                tracingId);
    }

    public CreateRejectedConnectionRequest withRejectedUser(UserIdDTO rejectedUser) {
        return new CreateRejectedConnectionRequest(requestId, rejectedByUser, rejectedUser, reason,
                tracingId);
    }

    public CreateRejectedConnectionRequest withReason(RejectedConnectionReasonDTO reason) {
        return new CreateRejectedConnectionRequest(requestId, rejectedByUser, rejectedUser, reason,
                tracingId);
    }

    public CreateRejectedConnectionRequest withTracingId(String tracingId) {
        return new CreateRejectedConnectionRequest(requestId, rejectedByUser, rejectedUser, reason,
                tracingId);
    }
}
