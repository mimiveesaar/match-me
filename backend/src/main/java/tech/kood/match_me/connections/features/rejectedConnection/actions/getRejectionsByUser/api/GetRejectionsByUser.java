package tech.kood.match_me.connections.features.rejectedConnection.actions.getRejectionsByUser.api;

import java.io.Serializable;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.jmolecules.architecture.cqrs.Command;
import tech.kood.match_me.common.domain.api.UserIdDTO;

public record GetRejectionsByUser(@NotNull @JsonProperty("request_id") UUID requestId,
        @NotNull @Valid @JsonProperty("rejected_user") UserIdDTO rejectedUser,
        @Nullable @JsonProperty("tracing_id") String tracingId) implements Serializable {

    public GetRejectionsByUser withRequestId(UUID requestId) {
        return new GetRejectionsByUser(requestId, rejectedUser, tracingId);
    }

    public GetRejectionsByUser withRejectedUser(UserIdDTO rejectedUser) {
        return new GetRejectionsByUser(requestId, rejectedUser, tracingId);
    }

    public GetRejectionsByUser withTracingId(String tracingId) {
        return new GetRejectionsByUser(requestId, rejectedUser, tracingId);
    }
}
