package tech.kood.match_me.user_management.features.refreshToken.domain.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record SharedSecretDTO(@NotNull UUID value) {

    @JsonCreator
    public static SharedSecretDTO of(String value) {
        return new SharedSecretDTO(UUID.fromString(value));
    }

    @Override
    @JsonValue
    public String toString() {
        return value.toString();
    }
}
