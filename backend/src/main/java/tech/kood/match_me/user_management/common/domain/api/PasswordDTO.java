package tech.kood.match_me.user_management.common.domain.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.validation.constraints.NotNull;
import org.jmolecules.architecture.layered.ApplicationLayer;
import tech.kood.match_me.user_management.common.validation.password.ValidPassword;

@ApplicationLayer
public record PasswordDTO(
        @NotNull @ValidPassword String value
) {

    @JsonCreator
    public static PasswordDTO of(String value) {
        return new PasswordDTO(value);
    }

    @Override
    @JsonValue
    public String toString() {
        return value;
    }
}
