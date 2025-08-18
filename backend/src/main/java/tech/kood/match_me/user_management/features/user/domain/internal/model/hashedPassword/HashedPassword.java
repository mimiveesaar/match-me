package tech.kood.match_me.user_management.features.user.domain.internal.model.hashedPassword;

import java.util.Objects;
import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.NotBlank;
import org.jmolecules.architecture.layered.DomainLayer;
import org.jmolecules.ddd.types.ValueObject;

@DomainLayer
public final class HashedPassword implements ValueObject {

    @NotBlank
    private final String hash;

    @NotBlank
    private final String salt;

    @Nonnull
    public String getHash() {
        return hash;
    }

    @Nonnull
    public String getSalt() {
        return salt;
    }

    HashedPassword(String hash, String salt) {
        this.hash = hash;
        this.salt = salt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof HashedPassword))
            return false;
        HashedPassword that = (HashedPassword) o;

        return this.hashCode() == that.hashCode();
    }

    @Override
    public int hashCode() {
        return Objects.hash(hash, salt);
    }

    @Override
    public String toString() {
        return "HashedPassword{" + "hash='" + hash + '\'' + ", salt='" + salt + '\'' + '}';
    }
}
