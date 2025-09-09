package tech.kood.match_me.user_management.features.user.actions.registerUser.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.jmolecules.architecture.cqrs.Command;
import org.jmolecules.architecture.layered.ApplicationLayer;
import tech.kood.match_me.user_management.common.domain.api.EmailDTO;
import tech.kood.match_me.user_management.common.domain.api.PasswordDTO;

@Command
@ApplicationLayer
public record RegisterUserRequest(@Valid @NotNull @JsonProperty("email") EmailDTO email,
                                  @Valid @NotNull @JsonProperty("password") PasswordDTO password) {
}