package tech.kood.match_me.user_management.features.accessToken.actions.createAccessToken.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.jmolecules.architecture.cqrs.Command;
import tech.kood.match_me.user_management.features.refreshToken.domain.api.RefreshTokenSecretDTO;

@Command
public record CreateAccessTokenRequest(@NotNull @Valid @JsonProperty("secret") RefreshTokenSecretDTO secret) {
}