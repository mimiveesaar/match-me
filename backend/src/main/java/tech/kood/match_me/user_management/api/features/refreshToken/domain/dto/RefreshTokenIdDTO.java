package tech.kood.match_me.user_management.api.features.refreshToken.domain.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.validation.constraints.NotNull;
import org.jmolecules.architecture.layered.ApplicationLayer;

import java.util.UUID;

@ApplicationLayer
public record RefreshTokenIdDTO(@NotNull UUID value) {

    @JsonCreator
    public static RefreshTokenIdDTO of(String value) {
        return new RefreshTokenIdDTO(UUID.fromString(value));
    }

    @JsonValue
    public String getValue() {
        return value.toString();
    }
}
