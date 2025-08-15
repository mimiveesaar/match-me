package tech.kood.match_me.user_management.internal.features.user.domain.model.email;

import jakarta.validation.constraints.NotBlank;
import org.jmolecules.architecture.layered.DomainLayer;
import org.jmolecules.ddd.types.ValueObject;

import java.util.Objects;

@DomainLayer
public final class Email implements ValueObject {

    @NotBlank
    @jakarta.validation.constraints.Email
    private final String value;

    Email(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Email that = (Email) o;
        return this.hashCode() == that.hashCode();
    }

    @Override
    public int hashCode() {
        return Objects.hash(value.toLowerCase());
    }

    @Override
    public String toString() {
        return value;
    }
}
