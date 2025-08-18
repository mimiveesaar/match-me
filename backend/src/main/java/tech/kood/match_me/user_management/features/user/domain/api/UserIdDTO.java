package tech.kood.match_me.user_management.features.user.domain.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.validation.constraints.NotNull;
import org.jmolecules.architecture.layered.ApplicationLayer;

import java.util.UUID;

@ApplicationLayer
public record UserIdDTO(@NotNull UUID value) {

    @JsonCreator
    public static UserIdDTO of(String value) {
        return new UserIdDTO(UUID.fromString(value));
    }

    @Override
    @JsonValue
    public String toString() {
        return value.toString();
    }
}
