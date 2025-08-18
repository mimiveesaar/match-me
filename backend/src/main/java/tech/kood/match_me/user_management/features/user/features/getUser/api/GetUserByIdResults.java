package tech.kood.match_me.user_management.features.user.features.getUser.api;

import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import tech.kood.match_me.user_management.features.user.domain.internal.model.user.User;
import tech.kood.match_me.user_management.features.user.domain.internal.model.user.UserId;

import java.util.UUID;

public sealed interface GetUserByIdResults permits
        GetUserByIdResults.Success,
        GetUserByIdResults.UserNotFound,
        GetUserByIdResults.SystemError
{

    record Success(@NotNull UUID requestId, @NotNull User user, @Nullable String tracingId) implements GetUserByIdResults {}
    record UserNotFound(@NotNull UUID requestId, @NotNull @Valid UserId userId, @Nullable String tracingId) implements GetUserByIdResults {}
    record SystemError(@NotNull UUID requestId, @NotEmpty String message, @Nullable String tracingId) implements GetUserByIdResults { }
}
