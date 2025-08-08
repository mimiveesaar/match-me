package tech.kood.match_me.user_management.internal.domain.models;

import java.util.Objects;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.constraints.NotNull;
import tech.kood.match_me.user_management.internal.common.validation.DomainObjectInputValidator;

public final class UserId {


    @NotNull
    private final UUID value;

    @JsonValue
    public String getValue() {
        return value.toString();
    }

    private UserId(UUID value) {
        this.value = value;
    }

    @JsonCreator
    public static UserId fromString(String value) {
        return of(UUID.fromString(value));
    }

    public static UserId of(UUID value) {
        var userId = new UserId(value);
        var violations = DomainObjectInputValidator.instance.validate(userId);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException("Invalid UserId: " + violations, violations);
        }
        return userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof UserId))
            return false;
        UserId userId = (UserId) o;
        return value.equals(userId.value);
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
