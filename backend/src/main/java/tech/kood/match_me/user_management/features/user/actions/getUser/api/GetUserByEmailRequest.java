package tech.kood.match_me.user_management.features.user.actions.getUser.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.jmolecules.architecture.layered.ApplicationLayer;
import tech.kood.match_me.user_management.common.domain.api.EmailDTO;

@ApplicationLayer
public record GetUserByEmailRequest(@NotNull @Valid @JsonProperty("email") EmailDTO email) {
}