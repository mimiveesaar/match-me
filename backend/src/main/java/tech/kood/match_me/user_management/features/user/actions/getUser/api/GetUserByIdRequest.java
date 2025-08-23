package tech.kood.match_me.user_management.features.user.actions.getUser.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.jmolecules.architecture.cqrs.QueryModel;
import org.jmolecules.architecture.layered.ApplicationLayer;
import tech.kood.match_me.user_management.common.domain.api.UserIdDTO;

import java.util.UUID;

@QueryModel
@ApplicationLayer
public record GetUserByIdRequest(@NotNull @JsonProperty("request_id") UUID requestId,
                                 @NotNull @Valid @JsonProperty("user_id") UserIdDTO userId,
                                 @Nullable @JsonProperty("tracing_id") String tracingId) {

    public GetUserByIdRequest(UserIdDTO userId, @Nullable String tracingId) {
        this(UUID.randomUUID(), userId, tracingId);
    }

    public GetUserByIdRequest withRequestId(UUID requestId) {
        return new GetUserByIdRequest(requestId, userId, tracingId);
    }

    public GetUserByIdRequest withUserId(UserIdDTO userId) {
        return new GetUserByIdRequest(requestId, userId, tracingId);
    }

    public GetUserByIdRequest withTracingId(String tracingId) {
        return new GetUserByIdRequest(requestId, userId, tracingId);
    }
}
