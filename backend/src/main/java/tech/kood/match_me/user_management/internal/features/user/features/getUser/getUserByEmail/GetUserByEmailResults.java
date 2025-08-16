package tech.kood.match_me.user_management.internal.features.user.features.getUser.getUserByEmail;

import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import tech.kood.match_me.user_management.internal.features.user.domain.model.User;
import tech.kood.match_me.user_management.internal.features.user.domain.model.email.Email;

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
