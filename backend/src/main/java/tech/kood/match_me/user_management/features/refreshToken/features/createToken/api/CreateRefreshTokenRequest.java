package tech.kood.match_me.user_management.features.refreshToken.features.createToken.api;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.jmolecules.architecture.cqrs.Command;
import tech.kood.match_me.user_management.features.user.domain.api.UserDTO;

@Command
public record CreateRefreshTokenRequest(@NotNull @JsonProperty("request_id") UUID requestId,
                                        @NotNull @Valid @JsonProperty("user") UserDTO user,
                                        @Nullable @JsonProperty("tracing_id") String tracingId) {

    public CreateRefreshTokenRequest(UserDTO user, @Nullable String tracingId) {
        this(UUID.randomUUID(), user, tracingId);
    }
}