package tech.kood.match_me.user_management.features.user.domain.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import org.jmolecules.architecture.layered.ApplicationLayer;

@ApplicationLayer
public record EmailDTO (
        @Email @NotNull @JsonValue String value
) {

    @JsonCreator
    public static EmailDTO of(String value) {
        return new EmailDTO(value);
    }
}
