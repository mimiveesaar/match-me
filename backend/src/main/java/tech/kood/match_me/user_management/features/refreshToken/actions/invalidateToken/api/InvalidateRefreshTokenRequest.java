package tech.kood.match_me.user_management.features.refreshToken.actions.invalidateToken.api;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.jmolecules.architecture.cqrs.Command;
import tech.kood.match_me.user_management.features.refreshToken.domain.api.RefreshTokenSecretDTO;


@Command
public record InvalidateRefreshTokenRequest(@NotNull @Valid @JsonProperty("secret") RefreshTokenSecretDTO secret) {

    public InvalidateRefreshTokenRequest withSecret(RefreshTokenSecretDTO secret) {
        return new InvalidateRefreshTokenRequest(secret);
    }
}
