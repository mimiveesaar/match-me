package tech.kood.match_me.user_management.features.accessToken.features.createAccessToken;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.jmolecules.event.annotation.DomainEvent;
import tech.kood.match_me.user_management.features.accessToken.domain.api.AccessTokenDTO;

@DomainEvent
public record AccessTokenCreatedEvent(@NotNull @Valid @JsonProperty("access_token") AccessTokenDTO accessToken) {
}
