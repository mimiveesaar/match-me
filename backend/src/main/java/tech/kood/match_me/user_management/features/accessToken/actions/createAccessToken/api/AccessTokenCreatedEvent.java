package tech.kood.match_me.user_management.features.accessToken.actions.createAccessToken.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.jmolecules.event.annotation.DomainEvent;
import tech.kood.match_me.user_management.common.domain.api.AccessTokenDTO;

@DomainEvent
public record AccessTokenCreatedEvent(@NotNull @Valid @JsonProperty("access_token") AccessTokenDTO accessToken) {
}
