package tech.kood.match_me.user_management.features.user.domain.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.modulith.NamedInterface;
import tech.kood.match_me.user_management.common.domain.api.EmailDTO;
import tech.kood.match_me.common.domain.api.UserIdDTO;

import java.time.Instant;

@NamedInterface
public record UserDTO(
        @Valid @NotNull @JsonProperty("id") UserIdDTO id,
        @Valid @NotNull @JsonProperty("email") EmailDTO email,
        @Valid @NotNull @JsonProperty("hashed_password") HashedPasswordDTO hashedPassword,
        @NotNull @JsonProperty("created_at") Instant createdAt,
        @NotNull @JsonProperty("updated_at") Instant updatedAt
) {
}
