package tech.kood.match_me.user_management.features.user.features.registerUser.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import tech.kood.match_me.user_management.features.user.domain.api.UserIdDTO;

public record UserRegisteredEvent(
        @NotNull @Valid @JsonProperty("user_id") UserIdDTO userId
) {
}