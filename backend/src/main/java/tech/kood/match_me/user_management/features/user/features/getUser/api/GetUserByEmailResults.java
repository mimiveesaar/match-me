package tech.kood.match_me.user_management.features.user.features.getUser.api;

import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import tech.kood.match_me.user_management.features.user.domain.internal.model.user.User;
import tech.kood.match_me.user_management.features.user.domain.internal.model.email.Email;

import java.util.UUID;

public sealed interface GetUserByEmailResults
        permits
        GetUserByEmailResults.Success,
        GetUserByEmailResults.UserNotFound,
        GetUserByEmailResults.SystemError  {

    record Success(@NotNull UUID requestId, @NotNull @Valid User user, @Nullable String tracingId) implements GetUserByEmailResults {}
    record UserNotFound(@NotNull UUID requestId, @NotNull @Valid Email email, @Nullable String tracingId) implements GetUserByEmailResults {}
    record SystemError(@NotNull UUID requestId, @NotEmpty String message, @Nullable String tracingId) implements GetUserByEmailResults {}
}
