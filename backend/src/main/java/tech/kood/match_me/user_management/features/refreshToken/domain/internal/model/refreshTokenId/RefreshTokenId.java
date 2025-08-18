package tech.kood.match_me.user_management.features.refreshToken.domain.internal.model.refreshTokenId;

import jakarta.validation.constraints.NotNull;
import org.jmolecules.architecture.layered.DomainLayer;
import org.jmolecules.ddd.types.Identifier;

import java.util.UUID;

@DomainLayer
public class RefreshTokenId implements Identifier {

    @NotNull
    private final UUID value;

    RefreshTokenId(UUID value) {
        this.value = value;
    }

    public UUID getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof RefreshTokenId))
            return false;
        RefreshTokenId that = (RefreshTokenId) o;
        return this.hashCode() == that.hashCode();
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
