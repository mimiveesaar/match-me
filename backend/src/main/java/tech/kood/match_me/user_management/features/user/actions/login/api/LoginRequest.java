package tech.kood.match_me.user_management.features.user.actions.login.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.jmolecules.architecture.cqrs.Command;
import tech.kood.match_me.user_management.common.domain.api.EmailDTO;
import tech.kood.match_me.user_management.common.domain.api.PasswordDTO;


@Command
public record LoginRequest(@NotNull @Valid @JsonProperty("email") EmailDTO email,
                           @NotNull @Valid @JsonProperty("password") PasswordDTO password) {
}
