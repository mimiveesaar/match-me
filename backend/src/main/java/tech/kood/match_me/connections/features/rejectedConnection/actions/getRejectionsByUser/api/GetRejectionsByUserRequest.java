package tech.kood.match_me.connections.features.rejectedConnection.actions.getRejectionsByUser.api;

import java.io.Serializable;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import tech.kood.match_me.common.domain.api.UserIdDTO;

public record GetRejectionsByUserRequest(@NotNull @JsonProperty("request_id") UUID requestId,
                                         @NotNull @Valid @JsonProperty("rejected_user") UserIdDTO rejectedUser,
                                         @Nullable @JsonProperty("tracing_id") String tracingId) implements Serializable {

    public GetRejectionsByUserRequest withRequestId(UUID requestId) {
        return new GetRejectionsByUserRequest(requestId, rejectedUser, tracingId);
    }

    public GetRejectionsByUserRequest withRejectedUser(UserIdDTO rejectedUser) {
        return new GetRejectionsByUserRequest(requestId, rejectedUser, tracingId);
    }

    public GetRejectionsByUserRequest withTracingId(String tracingId) {
        return new GetRejectionsByUserRequest(requestId, rejectedUser, tracingId);
    }
}
