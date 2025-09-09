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

    public RegisterUserRequest withEmail(String email) {
        return new RegisterUserRequest(new EmailDTO(email), password);
    }

    public RegisterUserRequest withEmail(EmailDTO email) {
        return new RegisterUserRequest(email, password);
    }

    public RegisterUserRequest withPassword(String password) {
        return new RegisterUserRequest(email, new PasswordDTO(password));
    }
    public RegisterUserRequest withPassword(PasswordDTO password) {
        return new RegisterUserRequest(email, password);
    }
}