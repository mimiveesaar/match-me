package tech.kood.match_me.user_management.features.user.actions.registerUser.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.jmolecules.event.annotation.DomainEvent;
import tech.kood.match_me.user_management.common.domain.api.UserIdDTO;

@DomainEvent
public record UserRegisteredEvent(
        @NotNull @Valid @JsonProperty("user_id") UserIdDTO userId
) {}