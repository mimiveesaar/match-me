package tech.kood.match_me.user_management.features.user.features.getUser.api;

import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.jmolecules.architecture.cqrs.QueryModel;
import org.jmolecules.architecture.layered.ApplicationLayer;
import tech.kood.match_me.user_management.features.user.domain.internal.model.user.UserId;

import java.util.UUID;

@QueryModel
@ApplicationLayer
public record GetUserByIdRequest(@NotNull UUID requestId, @NotNull @Valid UserId userId, @Nullable String tracingId) {

    public GetUserByIdRequest withRequestId(UUID requestId) {
        return new GetUserByIdRequest (requestId, userId, tracingId);
    }

    public GetUserByIdRequest withUserId(UserId userId) {
        return new GetUserByIdRequest (requestId, userId, tracingId);
    }

    public GetUserByIdRequest withTracingId(String tracingId) {
        return new GetUserByIdRequest (requestId, userId, tracingId);
    }
}
