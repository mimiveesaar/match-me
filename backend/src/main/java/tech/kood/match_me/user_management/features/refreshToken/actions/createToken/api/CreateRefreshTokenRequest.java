package tech.kood.match_me.user_management.features.refreshToken.actions.createToken.api;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.jmolecules.architecture.cqrs.Command;
import tech.kood.match_me.user_management.common.domain.api.UserIdDTO;

@Command
public record CreateRefreshTokenRequest(@NotNull @JsonProperty("request_id") UUID requestId,
                                        @NotNull @Valid @JsonProperty("userId") UserIdDTO userId,
                                        @Nullable @JsonProperty("tracing_id") String tracingId) {

    public CreateRefreshTokenRequest(UserIdDTO userId, @Nullable String tracingId) {
        this(UUID.randomUUID(), userId, tracingId);
    }
}