package tech.kood.match_me.user_management.features.refreshToken.domain.internal.model.sharedSecret;

import jakarta.validation.constraints.NotNull;

import java.util.Objects;
import java.util.UUID;

public class SharedSecret {

    @NotNull
    private final UUID value;

    SharedSecret(UUID value) {
        this.value = value;
    }

    public UUID getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        SharedSecret that = (SharedSecret) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
