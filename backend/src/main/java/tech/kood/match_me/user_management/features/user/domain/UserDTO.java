package tech.kood.match_me.user_management.features.user.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import tech.kood.match_me.user_management.common.domain.api.EmailDTO;
import tech.kood.match_me.common.domain.api.UserIdDTO;
import tech.kood.match_me.user_management.features.user.domain.internal.user.UserState;

import java.time.Instant;

public record UserDTO(
        @Valid @NotNull @JsonProperty("id") UserIdDTO id,
        @Valid @NotNull @JsonProperty("email") EmailDTO email,
        @Valid @NotNull @JsonProperty("hashed_password") HashedPasswordDTO hashedPassword,
        @NotNull @JsonProperty("user_state_code") UserStateDTO userState,
        @NotNull @JsonProperty("created_at") Instant createdAt,
        @NotNull @JsonProperty("updated_at") Instant updatedAt
) {
}