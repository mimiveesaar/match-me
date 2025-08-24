package tech.kood.match_me.connections.features.rejectedConnection.actions.getRejectedUsersByUser.api;

import java.io.Serializable;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.jmolecules.architecture.cqrs.Command;
import tech.kood.match_me.common.domain.api.UserIdDTO;

public record GetRejectedUsersByUser(@NotNull @JsonProperty("request_id") UUID requestId,
        @NotNull @Valid @JsonProperty("rejected_by_user") UserIdDTO rejectedByUser,
        @Nullable @JsonProperty("tracing_id") String tracingId) implements Serializable {

    public GetRejectedUsersByUser withRequestId(UUID requestId) {
        return new GetRejectedUsersByUser(requestId, rejectedByUser, tracingId);
    }

    public GetRejectedUsersByUser withRejectedByUser(UserIdDTO rejectedByUser) {
        return new GetRejectedUsersByUser(requestId, rejectedByUser, tracingId);
    }

    public GetRejectedUsersByUser withTracingId(String tracingId) {
        return new GetRejectedUsersByUser(requestId, rejectedByUser, tracingId);
    }
}
