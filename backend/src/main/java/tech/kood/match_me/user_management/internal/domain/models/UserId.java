package tech.kood.match_me.user_management.internal.domain.models;

import java.util.Objects;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public final class UserId {
    public final UUID value;

    public UserId(UUID value) {
        if (value == null)
            throw new IllegalArgumentException("UserId cannot be null");
        this.value = value;
    }

    @JsonCreator
    public UserId(String value) {
        if (value == null || value.isEmpty())
            throw new IllegalArgumentException("UserId cannot be null or empty");
        this.value = UUID.fromString(value);
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
    @JsonValue
    public String toString() {
        return value.toString();
    }
}
