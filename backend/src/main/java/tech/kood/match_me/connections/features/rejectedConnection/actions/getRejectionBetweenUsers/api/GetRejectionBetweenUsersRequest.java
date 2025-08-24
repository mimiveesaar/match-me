package tech.kood.match_me.connections.features.rejectedConnection.actions.getRejectionBetweenUsers.api;

import java.io.Serializable;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import tech.kood.match_me.common.domain.api.UserIdDTO;
import tech.kood.match_me.connections.features.rejectedConnection.actions.getRejectionBetweenUsers.api.validation.ValidGetRejectionBetweenUsers;

@ValidGetRejectionBetweenUsers
public record GetRejectionBetweenUsersRequest(@NotNull @JsonProperty("request_id") UUID requestId,
                                              @NotNull @Valid @JsonProperty("user1") UserIdDTO user1,
                                              @NotNull @Valid @JsonProperty("user2") UserIdDTO user2,
                                              @Nullable @JsonProperty("tracing_id") String tracingId) implements Serializable {

    public GetRejectionBetweenUsersRequest withRequestId(UUID requestId) {
        return new GetRejectionBetweenUsersRequest(requestId, user1, user2, tracingId);
    }

    public GetRejectionBetweenUsersRequest withUser1(UserIdDTO user1) {
        return new GetRejectionBetweenUsersRequest(requestId, user1, user2, tracingId);
    }

    public GetRejectionBetweenUsersRequest withUser2(UserIdDTO user2) {
        return new GetRejectionBetweenUsersRequest(requestId, user1, user2, tracingId);
    }

    public GetRejectionBetweenUsersRequest withTracingId(String tracingId) {
        return new GetRejectionBetweenUsersRequest(requestId, user1, user2, tracingId);
    }
}
