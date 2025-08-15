package tech.kood.match_me.user_management.internal.features.user.domain.model.userId;

import java.util.Objects;
import java.util.UUID;
import jakarta.validation.constraints.NotNull;
import org.jmolecules.ddd.types.Identifier;

public class UserId implements Identifier {

    @NotNull
    private final UUID value;

    UserId(UUID value) {
        this.value = value;
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
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
