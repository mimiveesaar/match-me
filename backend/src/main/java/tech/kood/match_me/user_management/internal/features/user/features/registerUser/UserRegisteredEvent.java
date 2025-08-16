package tech.kood.match_me.user_management.internal.features.user.features.registerUser;

import jakarta.validation.constraints.NotNull;
import tech.kood.match_me.user_management.internal.features.user.domain.model.userId.UserId;

public record UserRegisteredEvent(
        @NotNull UserId userId
) {
}