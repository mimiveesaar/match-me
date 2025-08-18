package tech.kood.match_me.user_management.features.refreshToken.domain.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record RefreshTokenSecretDTO(@NotNull UUID value) {

    @JsonCreator
    public static RefreshTokenSecretDTO of(String value) {
        return new RefreshTokenSecretDTO(UUID.fromString(value));
    }

    @Override
    @JsonValue
    public String toString() {
        return value.toString();
    }
}
