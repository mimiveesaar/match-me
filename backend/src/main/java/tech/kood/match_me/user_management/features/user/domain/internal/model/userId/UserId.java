package tech.kood.match_me.user_management.features.user.domain.internal.model.userId;

import java.util.UUID;
import jakarta.validation.constraints.NotNull;
import org.jmolecules.architecture.layered.DomainLayer;
import org.jmolecules.ddd.types.Identifier;

@DomainLayer
public class UserId implements Identifier {

    @NotNull
    private final UUID value;

    UserId(UUID value) {
        this.value = value;
    }

    public UUID getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof UserId))
            return false;
        UserId that = (UserId) o;
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
