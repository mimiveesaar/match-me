package tech.kood.match_me.user_management.api.features.user.domain.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.jmolecules.architecture.layered.ApplicationLayer;

@ApplicationLayer
public record PasswordDTO(
        String value
) {

    @JsonCreator
    public static PasswordDTO of(String value) {
        return new PasswordDTO(value);
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
