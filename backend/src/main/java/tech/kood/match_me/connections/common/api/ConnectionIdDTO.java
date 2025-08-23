package tech.kood.match_me.connections.common.api;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.validation.constraints.NotNull;
import org.jmolecules.architecture.layered.ApplicationLayer;
import org.springframework.modulith.NamedInterface;

import java.util.UUID;

@NamedInterface
@ApplicationLayer
public record ConnectionIdDTO(@NotNull UUID value) {

    @JsonCreator
    public static ConnectionIdDTO of(String value) {
        return new ConnectionIdDTO(UUID.fromString(value));
    }

    @JsonValue
    @Override
    public String toString() {
        return value.toString();
    }
}
