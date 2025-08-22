package tech.kood.match_me.user_management.features.refreshToken.domain.internal.model.refreshTokenSecret;

import jakarta.validation.constraints.NotNull;
import org.jmolecules.architecture.layered.DomainLayer;
import org.jmolecules.ddd.types.ValueObject;

import java.util.Objects;
import java.util.UUID;

@DomainLayer
public class RefreshTokenSecret implements ValueObject {

    @NotNull
    private final UUID value;

    RefreshTokenSecret(UUID value) {
        this.value = value;
    }

    public UUID getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        RefreshTokenSecret that = (RefreshTokenSecret) o;
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
