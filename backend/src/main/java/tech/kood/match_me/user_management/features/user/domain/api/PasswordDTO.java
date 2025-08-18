package tech.kood.match_me.user_management.features.user.domain.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.jmolecules.architecture.layered.ApplicationLayer;

@ApplicationLayer
public record PasswordDTO(
        @JsonValue String value
) {

    @JsonCreator
    public static PasswordDTO of(String value) {
        return new PasswordDTO(value);
    }
}
