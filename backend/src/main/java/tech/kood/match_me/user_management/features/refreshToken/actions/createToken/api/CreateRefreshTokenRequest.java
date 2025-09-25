package tech.kood.match_me.user_management.features.refreshToken.actions.createToken.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.jmolecules.architecture.cqrs.Command;
import tech.kood.match_me.common.domain.api.UserIdDTO;

@Command
public record CreateRefreshTokenRequest(@NotNull @Valid @JsonProperty("userId") UserIdDTO userId) {
}