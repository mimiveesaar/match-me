package tech.kood.match_me.common.domain.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.validation.constraints.NotNull;
import org.jmolecules.architecture.layered.ApplicationLayer;
import org.springframework.modulith.NamedInterface;

import java.util.UUID;

@NamedInterface
@ApplicationLayer
public record UserIdDTO(@NotNull UUID value) {

    @JsonCreator
    public static UserIdDTO of(String value) {
        return new UserIdDTO(UUID.fromString(value));
    }

    public static UserIdDTO from(UUID value) {
        return new UserIdDTO(value);
    }

    @Override
    @JsonValue
    public String toString() {
        return value.toString();
    }

    public UUID value() {
        return value;
    }

}
